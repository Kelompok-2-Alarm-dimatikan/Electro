let cart = JSON.parse(localStorage.getItem('electro_cart') || '[]');
let currentModal = null;
let modalQty = 1;

function updateCartStates() {
    const cards = document.querySelectorAll('.product-card');
    cards.forEach(card => {
        const id = card.dataset.id;
        const cartItem = cart.find(item => item.id === id);
        
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
                btnAdd.innerHTML = `
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="21" r="1"></circle><circle cx="20" cy="21" r="1"></circle><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path></svg>
                    Checkout
                `;
                btnAdd.classList.add('checkout-mode');
                btnAdd.disabled = false;
                btnAdd.onclick = (e) => {
                    e.stopPropagation();
                    toggleCart();
                };
            }
        } else {
            if (inCartBadge) inCartBadge.remove();
            
            if (btnAdd) {
                btnAdd.innerHTML = `
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"></path><line x1="3" y1="6" x2="21" y1="6"></line><path d="M16 10a4 4 0 0 1-8 0"></path></svg>
                    Add to Cart
                `;
                btnAdd.classList.remove('checkout-mode');
                const stok = parseInt(card.dataset.stok);
                btnAdd.disabled = (stok === 0);
                btnAdd.onclick = (e) => {
                    e.stopPropagation();
                    addToCartDirect(card);
                };
            }
        }
    });
}

function updateModalBtnState() {
    if (!currentModal) return;
    const btnAddModal = document.getElementById('btnAddModal');
    if (!btnAddModal) return;
    
    const cartItem = cart.find(item => item.id === currentModal.id);
    if (cartItem) {
        btnAddModal.textContent = "Checkout";
        btnAddModal.onclick = () => {
            closeModal();
            toggleCart();
        };
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

document.addEventListener('DOMContentLoaded', () => {
    renderCartCount();
    renderCart();
    setupSearchAndFilters();
    updateCartStates();
    updateProductCount();
});

let originalCardOrder = [];

function updateProductCount() {
    const allCards = document.querySelectorAll('.product-card');
    let count = 0;
    allCards.forEach(c => {
        if (c.style.display !== 'none') count++;
    });
    document.getElementById('countValue').textContent = count;
}

function setupSearchAndFilters() {
    const searchInput = document.getElementById('searchInput');
    const chips = document.querySelectorAll('.chip');
    const sortSelect = document.getElementById('sortSelect');
    const grid = document.getElementById('productGrid');

    originalCardOrder = Array.from(grid.querySelectorAll('.product-card'));

    const runFilters = () => {
        const query = searchInput.value.toLowerCase().trim();
        const activeChip = document.querySelector('.chip.active');
        const category = activeChip ? activeChip.dataset.category : 'all';
        const sortOrder = sortSelect.value;

        let cards = Array.from(grid.querySelectorAll('.product-card'));
        let visibleCount = 0;

        cards.forEach(card => {
            const nama = (card.dataset.nama || '').toLowerCase();
            const merk = (card.dataset.merk || '').toLowerCase();
            const kategori = (card.dataset.kategori || '');

            const matchesSearch = !query || nama.includes(query) || merk.includes(query);
            const matchesCategory = (category === 'all') || (kategori === category);

            if (matchesSearch && matchesCategory) {
                card.style.display = 'flex';
                visibleCount++;
            } else {
                card.style.display = 'none';
            }
        });

        if (sortOrder === 'default') {
            originalCardOrder.forEach(card => grid.appendChild(card));
        } else {
            const visibleCards = cards.filter(c => c.style.display !== 'none');
            visibleCards.sort((a, b) => {
                const priceA = parseFloat(a.dataset.harga) || 0;
                const priceB = parseFloat(b.dataset.harga) || 0;
                return sortOrder === 'low-high' ? priceA - priceB : priceB - priceA;
            });
            visibleCards.forEach(card => grid.appendChild(card));
        }

        document.getElementById('countValue').textContent = visibleCount;
    };

    if (searchInput) {
        searchInput.addEventListener('input', runFilters);
        searchInput.addEventListener('search', runFilters);
    }

    chips.forEach(chip => {
        chip.addEventListener('click', () => {
            chips.forEach(c => c.classList.remove('active'));
            chip.classList.add('active');
            runFilters();
        });
    });

    if (sortSelect) sortSelect.addEventListener('change', runFilters);
}

function buildModalDesc(deskripsi, spesifikasi) {
    let html = '';

    if (deskripsi) {
        html += `<p class="modal-desc-intro">${deskripsi}</p>`;
    }

    if (spesifikasi) {
        // Parse "Key: Value | Key: Value" format
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

function showDetail(cardElement) {
    const id        = cardElement.dataset.id;
    const nama      = cardElement.dataset.nama;
    const merk      = cardElement.dataset.merk;
    const kategori  = cardElement.dataset.kategori;
    const harga     = parseFloat(cardElement.dataset.harga);
    const stok      = parseInt(cardElement.dataset.stok);
    const deskripsi = cardElement.dataset.deskripsi || '';
    const spesifikasi = cardElement.dataset.spesifikasi || '';

    // Ambil imageUrl dari data attribute 
    const imgSrc = cardElement.dataset.image
        || cardElement.querySelector('.card-image-area img').src;

    currentModal = { id, nama, merk, kategori, harga, stok, imgSrc };
    modalQty = 1;

    document.getElementById('modalNama').textContent = nama;
    document.getElementById('modalMerk').textContent = merk;
    document.getElementById('modalKategori').textContent = kategori;
    document.getElementById('modalHarga').textContent = formatRp(harga);
    document.getElementById('modalStok').textContent = `Stok: ${stok} unit`;
    document.getElementById('modalImg').src = imgSrc;
    document.getElementById('modalImg').alt = nama;

    const descElement = document.getElementById('modalDesc');
    if (descElement) {
        descElement.innerHTML = buildModalDesc(deskripsi, spesifikasi);
    }

    document.getElementById('qtyValue').textContent = modalQty;
    updateModalBtnState();

    document.getElementById('productModal').classList.add('open');
    document.getElementById('modalOverlay').classList.add('open');
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

function toggleCart() {
    document.getElementById('cartSidebar').classList.toggle('open');
    document.getElementById('cartOverlay').classList.toggle('show');
}

function addToCartDirect(cardElement) {
    const id   = cardElement.dataset.id;
    const nama = cardElement.dataset.nama;
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
    const existing = cart.find(item => item.id === id);
    if (existing) {
        existing.qty += qty;
    } else {
        cart.push({ id, name, price, qty });
    }
    localStorage.setItem('electro_cart', JSON.stringify(cart));
    renderCartCount();
    renderCart();
    updateCartStates();
    updateModalBtnState();
}

function renderCartCount() {
    const count = cart.reduce((sum, item) => sum + item.qty, 0);
    document.getElementById('cartCount').textContent = count;
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
            </div>
        `;
    });
    document.getElementById('cartTotal').textContent = formatRp(total);
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

// AUTO-APPLY FILTER
document.addEventListener('DOMContentLoaded', () => {
    const cat = sessionStorage.getItem('filterCat');
    if (cat) {
        sessionStorage.removeItem('filterCat');
        const chip = document.querySelector(`.chip[data-category="${cat}"]`);
        if (chip) chip.click();
    }
});