// ─── Cart State ─────────────────────────────────────────────────────────────
let cart = JSON.parse(localStorage.getItem('electro_cart') || '[]');
let currentModal = null;
let modalQty = 1;

// ─── Filter State (dipertahankan di JS, tidak perlu reload) ─────────────────
let activeKategori = 'all';
let activeSort = 'default';
let activeNama = '';
let searchDebounceTimer = null;

// ─── Fallback gambar per kategori ───────────────────────────────────────────
const FALLBACK_IMAGES = {
    Hp:        'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?q=80&w=400',
    Laptop:    'https://images.unsplash.com/photo-1603302576837-37561b2e2302?q=80&w=400',
    Tablet:    'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?q=80&w=400',
    Tv:        'https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?q=80&w=400',
    Ac:        'https://images.unsplash.com/photo-1621905251189-08b45d6a269e?q=80&w=400',
    Blender:   'https://images.unsplash.com/photo-1585515320310-259814833e62?q=80&w=400',
    Kulkas:    'https://images.unsplash.com/photo-1584568694244-14fbdf83bd30?q=80&w=400',
    Headphone: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=400',
};
const FALLBACK_DEFAULT = 'https://images.unsplash.com/photo-1546868871-7041f2a55e12?q=80&w=400';

// ─── Spec chip label per kategori ───────────────────────────────────────────
const SPEC_CHIPS = {
    Hp: '5G Ready', Laptop: 'Fast SSD', Tablet: 'Portable',
    Tv: '4K Display', Ac: 'Inverter', Headphone: 'Noise Cancel',
};

// ─── Init ────────────────────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    // Ambil state awal dari URL (supaya deep-link / refresh tetap benar)
    const params = new URLSearchParams(window.location.search);
    activeNama     = params.get('nama')     || '';
    activeSort     = params.get('sort')     || 'default';
    activeKategori = params.get('kategori') || 'all';

    // Terapkan ke elemen UI
    const searchInput = document.getElementById('searchInput');
    if (searchInput) searchInput.value = activeNama;

    const sortSelect = document.getElementById('sortSelect');
    if (sortSelect) sortSelect.value = activeSort;

    setActiveChip(activeKategori);

    // Event listeners
    setupSearchAndFilters();

    // Load produk pertama kali dari backend
    fetchAndRender();

    // Cart
    renderCartCount();
    renderCart();
});

// ─── Ambil data dari backend Java via AJAX (tanpa reload halaman) ─────────────
async function fetchAndRender() {
    const grid = document.getElementById('productGrid');
    if (!grid) return;

    // Tampilkan skeleton loading
    grid.innerHTML = `<div class="loading-state">
        <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="spin-icon"><path d="M21 12a9 9 0 1 1-6.219-8.56"/></svg>
        <span>Memuat produk...</span>
    </div>`;

    try {
        // Kirim ke backend Java (Linear Search + Bubble Sort tetap di server)
        const url = new URL('/api/product', window.location.origin);
        if (activeNama)                           url.searchParams.set('nama',     activeNama);
        if (activeKategori && activeKategori !== 'all') url.searchParams.set('kategori', activeKategori);
        if (activeSort && activeSort !== 'default')     url.searchParams.set('sort',     activeSort);

        const response = await fetch(url.toString(), { credentials: 'same-origin' });
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        const products = await response.json();

        renderProductGrid(products);
        updateCartStates();
    } catch (err) {
        grid.innerHTML = `<div class="loading-state" style="color:#ef4444">
            <span>Gagal memuat produk. Coba refresh halaman.</span>
        </div>`;
        console.error('fetchAndRender error:', err);
    }
}

// ─── Render kartu produk ke DOM ───────────────────────────────────────────────
function renderProductGrid(products) {
    const grid = document.getElementById('productGrid');
    if (!grid) return;

    document.getElementById('countValue').textContent = products.length;

    if (products.length === 0) {
        grid.innerHTML = `<div class="loading-state">
            <span>Tidak ada produk yang ditemukan.</span>
        </div>`;
        return;
    }

    grid.innerHTML = products.map(p => buildCardHTML(p)).join('');

    // Pasang event onclick ke kartu yang baru dibuat
    grid.querySelectorAll('.product-card').forEach(card => {
        card.querySelector('.btn-detail')?.addEventListener('click', (e) => {
            e.stopPropagation();
            showDetailFromData(card.dataset);
        });
        card.querySelector('.btn-add-cart')?.addEventListener('click', (e) => {
            e.stopPropagation();
            addToCartDirect(card);
        });
    });
}

function buildCardHTML(p) {
    const imgSrc    = p.imageUrl || FALLBACK_DEFAULT;
    const fallback  = FALLBACK_IMAGES[p.kategori] || FALLBACK_DEFAULT;
    const specChip  = SPEC_CHIPS[p.kategori] || 'Smart Tech';
    const stokBadge = p.stok === 0
        ? `<span class="badge-stock empty">Habis</span>`
        : (p.stok <= 3 ? `<span class="badge-stock alert">Sisa ${p.stok}!</span>` : '');
    const addDisabled = p.stok === 0 ? 'disabled' : '';
    const hargaFmt = 'Rp ' + Math.round(p.harga).toLocaleString('id-ID');

    const cartItem = cart.find(c => String(c.id) === String(p.id));
    let btnHtml, inCartBadge = '';
    if (cartItem) {
        inCartBadge = `<span class="badge-stock badge-in-cart">🛒 ${cartItem.qty} di Keranjang</span>`;
        btnHtml = `<button class="btn-add-cart checkout-mode" data-action="checkout">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="21" r="1"></circle><circle cx="20" cy="21" r="1"></circle><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path></svg>
            Checkout
        </button>`;
    } else {
        btnHtml = `<button class="btn-add-cart" ${addDisabled}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"></path><line x1="3" y1="6" x2="21" y1="6"></line><path d="M16 10a4 4 0 0 1-8 0"></path></svg>
            Add to Cart
        </button>`;
    }

    // Encode data agar aman disimpan di data-* attribute
    const esc = (str) => String(str || '').replace(/"/g, '&quot;');

    return `
    <div class="product-card"
         data-id="${p.id}"
         data-nama="${esc(p.nama)}"
         data-merk="${esc(p.merk)}"
         data-kategori="${esc(p.kategori)}"
         data-harga="${p.harga}"
         data-stok="${p.stok}"
         data-image="${esc(imgSrc)}"
         data-deskripsi="${esc(p.deskripsi)}"
         data-spesifikasi="${esc(p.spesifikasi)}">

        <div class="card-badges">
            <span class="badge-cate">${esc(p.kategori)}</span>
            ${stokBadge}
            ${inCartBadge}
        </div>

        <div class="card-image-area">
            <img src="${esc(imgSrc)}" alt="${esc(p.nama)}"
                 onerror="this.src='${fallback}'">
            <div class="image-overlay-glow"></div>
        </div>

        <div class="card-content">
            <span class="product-brand">${esc(p.merk)}</span>
            <h3 class="product-name">${esc(p.nama)}</h3>
            <div class="product-specs-chips">
                <span class="spec-chip">Premium Quality</span>
                <span class="spec-chip">${specChip}</span>
                <span class="spec-chip">Garansi Resmi</span>
            </div>
            <div class="price-row">
                <span class="product-price">${hargaFmt}</span>
            </div>
        </div>

        <div class="card-footer">
            ${btnHtml}
            <button class="btn-detail">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
            </button>
        </div>
    </div>`;
}

// ─── Search + Filter + Sort (semua TANPA reload halaman) ─────────────────────
function setupSearchAndFilters() {
    const searchInput = document.getElementById('searchInput');
    const clearBtn    = document.getElementById('clearSearch');
    const chips       = document.querySelectorAll('.chip');
    const sortSelect  = document.getElementById('sortSelect');

    if (searchInput) {
        // Auto-search: ketik langsung cari, debounce 400ms
        searchInput.addEventListener('input', () => {
            activeNama = searchInput.value.trim();
            clearBtn?.classList.toggle('visible', activeNama.length > 0);
            clearTimeout(searchDebounceTimer);
            searchDebounceTimer = setTimeout(() => {
                pushState();
                fetchAndRender();
            }, 400);
        });

        // Enter: langsung tanpa tunggu debounce
        searchInput.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                clearTimeout(searchDebounceTimer);
                activeNama = searchInput.value.trim();
                pushState();
                fetchAndRender();
            }
        });
    }

    // Category chips: simpan state, fetch — TANPA reset search
    chips.forEach(chip => {
        chip.addEventListener('click', () => {
            activeKategori = chip.dataset.category;
            setActiveChip(activeKategori);
            clearTimeout(searchDebounceTimer);
            pushState();
            fetchAndRender();
        });
    });

    // Sort dropdown
    if (sortSelect) {
        sortSelect.addEventListener('change', () => {
            activeSort = sortSelect.value;
            pushState();
            fetchAndRender();
        });
    }
}

// Sinkronkan visual chip aktif
function setActiveChip(kategori) {
    document.querySelectorAll('.chip').forEach(c => {
        c.classList.toggle('active', c.dataset.category === kategori);
    });
}

// Update URL di address bar (tanpa reload) agar bisa di-refresh / share link
function pushState() {
    const url = new URL(window.location.href);
    if (activeNama)                                url.searchParams.set('nama',     activeNama);
    else                                           url.searchParams.delete('nama');
    if (activeKategori && activeKategori !== 'all') url.searchParams.set('kategori', activeKategori);
    else                                           url.searchParams.delete('kategori');
    if (activeSort && activeSort !== 'default')    url.searchParams.set('sort',     activeSort);
    else                                           url.searchParams.delete('sort');
    history.pushState({}, '', url.toString());
}

// Tombol clear search
function clearAndSubmit() {
    const searchInput = document.getElementById('searchInput');
    if (searchInput) searchInput.value = '';
    document.getElementById('clearSearch')?.classList.remove('visible');
    activeNama = '';
    clearTimeout(searchDebounceTimer);
    pushState();
    fetchAndRender();
}

// applySort dipanggil dari sortSelect.onchange (sudah dihandle di setupSearchAndFilters)
function applySort(sortValue) {
    activeSort = sortValue;
    pushState();
    fetchAndRender();
}

// ─── Modal ────────────────────────────────────────────────────────────────────
function showDetailFromData(dataset) {
    const id          = dataset.id;
    const nama        = dataset.nama;
    const merk        = dataset.merk;
    const kategori    = dataset.kategori;
    const harga       = parseFloat(dataset.harga);
    const stok        = parseInt(dataset.stok);
    const deskripsi   = dataset.deskripsi  || '';
    const spesifikasi = dataset.spesifikasi || '';
    const imgSrc      = dataset.image || FALLBACK_IMAGES[kategori] || FALLBACK_DEFAULT;

    currentModal = { id, nama, merk, kategori, harga, stok, imgSrc };
    modalQty = 1;

    document.getElementById('modalNama').textContent     = nama;
    document.getElementById('modalMerk').textContent     = merk;
    document.getElementById('modalKategori').textContent = kategori;
    document.getElementById('modalHarga').textContent    = formatRp(harga);
    document.getElementById('modalStok').textContent     = `Stok: ${stok} unit`;
    document.getElementById('modalImg').src              = imgSrc;
    document.getElementById('modalImg').alt              = nama;

    const descElement = document.getElementById('modalDesc');
    if (descElement) descElement.innerHTML = buildModalDesc(deskripsi, spesifikasi);

    document.getElementById('qtyValue').textContent = modalQty;
    updateModalBtnState();
    document.getElementById('productModal').classList.add('open');
    document.getElementById('modalOverlay').classList.add('open');
}

// Tetap kompatibel jika ada onclick lama di template
function showDetail(cardElement) {
    showDetailFromData(cardElement.dataset);
}

function closeModal() {
    document.getElementById('productModal').classList.remove('open');
    document.getElementById('modalOverlay').classList.remove('open');
    currentModal = null;
}

function changeQty(delta) {
    if (!currentModal) return;
    modalQty = Math.max(1, Math.min(currentModal.stok, modalQty + delta));
    document.getElementById('qtyValue').textContent = modalQty;
}

function buildModalDesc(deskripsi, spesifikasi) {
    let html = '';
    if (deskripsi) html += `<p class="modal-desc-intro">${deskripsi}</p>`;
    if (spesifikasi) {
        const pairs = spesifikasi.split('|').map(s => s.trim()).filter(Boolean);
        if (pairs.length > 0) {
            html += `<div class="modal-specs-grid">`;
            pairs.forEach(pair => {
                const colonIdx = pair.indexOf(':');
                if (colonIdx !== -1) {
                    const label = pair.substring(0, colonIdx).trim();
                    const value = pair.substring(colonIdx + 1).trim();
                    html += `<div class="spec-item"><span class="spec-label">${label}</span><span class="spec-value">${value}</span></div>`;
                } else {
                    html += `<div class="spec-item"><span class="spec-value" style="grid-column:1/-1">${pair}</span></div>`;
                }
            });
            html += `</div>`;
        }
    }
    if (!html) {
        html = `<p class="modal-desc-intro">Produk berkualitas premium dengan garansi resmi.</p>
                <div class="modal-specs-grid">
                    <div class="spec-item"><span class="spec-label">Kualitas</span><span class="spec-value">Premium Standard</span></div>
                    <div class="spec-item"><span class="spec-label">Garansi</span><span class="spec-value">Resmi 1 Tahun</span></div>
                </div>`;
    }
    return html;
}

// ─── Cart ─────────────────────────────────────────────────────────────────────
function updateCartStates() {
    document.querySelectorAll('.product-card').forEach(card => {
        const id = card.dataset.id;
        const cartItem = cart.find(item => String(item.id) === String(id));
        const badgesContainer = card.querySelector('.card-badges');
        if (!badgesContainer) return;
        let inCartBadge = badgesContainer.querySelector('.badge-in-cart');
        const btnAdd = card.querySelector('.btn-add-cart');
        if (cartItem) {
            if (!inCartBadge) {
                inCartBadge = document.createElement('span');
                inCartBadge.className = 'badge-stock badge-in-cart';
                badgesContainer.appendChild(inCartBadge);
            }
            inCartBadge.innerHTML = `🛒 ${cartItem.qty} di Keranjang`;
            if (btnAdd) {
                btnAdd.innerHTML = `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="21" r="1"></circle><circle cx="20" cy="21" r="1"></circle><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path></svg> Checkout`;
                btnAdd.classList.add('checkout-mode');
                btnAdd.disabled = false;
                btnAdd.onclick = (e) => { e.stopPropagation(); toggleCart(); };
            }
        } else {
            if (inCartBadge) inCartBadge.remove();
            if (btnAdd) {
                btnAdd.innerHTML = `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"></path><line x1="3" y1="6" x2="21" y1="6"></line><path d="M16 10a4 4 0 0 1-8 0"></path></svg> Add to Cart`;
                btnAdd.classList.remove('checkout-mode');
                const stok = parseInt(card.dataset.stok);
                btnAdd.disabled = (stok === 0);
                btnAdd.onclick = (e) => { e.stopPropagation(); addToCartDirect(card); };
            }
        }
    });
}

function updateModalBtnState() {
    if (!currentModal) return;
    const btnAddModal = document.getElementById('btnAddModal');
    if (!btnAddModal) return;
    const cartItem = cart.find(item => String(item.id) === String(currentModal.id));
    if (cartItem) {
        btnAddModal.textContent = "Checkout";
        btnAddModal.onclick = () => { closeModal(); toggleCart(); };
    } else {
        btnAddModal.textContent = "Masukkan Keranjang";
        if (currentModal.stok === 0) {
            btnAddModal.disabled = true;
            btnAddModal.style.opacity = '0.5';
            btnAddModal.style.cursor = 'not-allowed';
        } else {
            btnAddModal.disabled = false;
            btnAddModal.style.opacity = '1';
            btnAddModal.style.cursor = 'pointer';
            btnAddModal.onclick = () => addToCartFromModal();
        }
    }
}

function toggleCart() {
    document.getElementById('cartSidebar').classList.toggle('open');
    document.getElementById('cartOverlay').classList.toggle('show');
}

function addToCartDirect(cardElement) {
    const id    = cardElement.dataset.id;
    const nama  = cardElement.dataset.nama;
    const harga = parseFloat(cardElement.dataset.harga);
    pushToCart(id, nama, harga, 1);
    showToast(`🛒 ${nama} dimasukkan ke keranjang!`);
}

function addToCartFromModal() {
    if (!currentModal) return;
    pushToCart(currentModal.id, currentModal.nama, currentModal.harga, modalQty);
    closeModal();
    showToast(`🛒 ${currentModal.nama} dimasukkan ke keranjang!`);
}

function pushToCart(id, name, price, qty) {
    const existing = cart.find(item => String(item.id) === String(id));
    if (existing) {
        existing.qty += qty;
    } else {
        cart.push({ id: String(id), name, price, qty });
    }
    localStorage.setItem('electro_cart', JSON.stringify(cart));
    renderCartCount();
    renderCart();
    updateCartStates();
    updateModalBtnState();
}

function renderCartCount() {
    const count = cart.reduce((sum, item) => sum + item.qty, 0);
    const el = document.getElementById('cartCount');
    if (el) el.textContent = count;
}

function renderCart() {
    const container = document.getElementById('cartItems');
    if (!container) return;
    container.innerHTML = '';
    let total = 0;
    cart.forEach((item, index) => {
        total += item.price * item.qty;
        container.innerHTML += `
            <div class="cart-item-row">
                <div class="cart-item-info">
                    <div class="cart-item-name">${item.name}</div>
                    <div class="cart-item-meta">${item.qty} x ${formatRp(item.price)}</div>
                </div>
                <button class="btn-remove-item" onclick="removeFromCart(${index})">Hapus</button>
            </div>`;
    });
    const totalEl = document.getElementById('cartTotal');
    if (totalEl) totalEl.textContent = formatRp(total);
}

function removeFromCart(index) {
    cart.splice(index, 1);
    localStorage.setItem('electro_cart', JSON.stringify(cart));
    renderCartCount();
    renderCart();
    updateCartStates();
    updateModalBtnState();
}

function checkout() {
    if (cart.length === 0) return;
    window.location.href = '/checkout';
}

function formatRp(num) {
    return 'Rp ' + Math.round(num).toLocaleString('id-ID');
}

function showToast(msg) {
    const el = document.getElementById('toast');
    if (!el) return;
    el.textContent = msg;
    el.classList.add('show');
    setTimeout(() => el.classList.remove('show'), 2500);
}