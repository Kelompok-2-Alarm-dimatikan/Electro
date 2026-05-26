let cart = JSON.parse(localStorage.getItem('electro_cart') || '[]');
let currentModal = null;
let modalQty = 1;

const PRODUCT_IMAGES = {
    'Samsung S23 Ultra': 'https://www.static-src.com/wcsstore/Indraprastha/images/catalog/full//catalog-image/102/MTA-114674295/samsung_samsung_galaxy_s23_ultra_full02_hv6vvrfz.jpg',
    'Samsung J2 Prime': 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=600&q=80',
    'Xiaomi 13': 'https://images.unsplash.com/photo-1598327105666-5b89351cb31b?auto=format&fit=crop&w=600&q=80',
    'iPhone 14': 'https://images.unsplash.com/photo-1556656793-08538906a9f8?auto=format&fit=crop&w=600&q=80',
    'Asus ROG': 'https://images.unsplash.com/photo-1603302576837-37561b2e2302?auto=format&fit=crop&w=600&q=80',
    'Acer Predator': 'https://images.unsplash.com/photo-1593640408182-31c70c8268f5?auto=format&fit=crop&w=600&q=80',
    'Lenovo Legion': 'https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?auto=format&fit=crop&w=600&q=80',
    'iPad Pro': 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?auto=format&fit=crop&w=600&q=80',
    'Samsung Galaxy Tab': 'https://images.unsplash.com/photo-1585790050230-5dd28404ccb9?auto=format&fit=crop&w=600&q=80'
};

const PRODUCT_DESCRIPTIONS = {
    'Samsung S23 Ultra': `
        <p class="modal-desc-intro">Samsung Galaxy S23 Ultra adalah smartphone flagship yang dilengkapi dengan S Pen bawaan, ditenagai chipset khusus Snapdragon 8 Gen 2, dan kamera utama 200MP dengan zoom hingga 100x. Ponsel ini memiliki layar 6.8 inci QHD+ Dynamic AMOLED 2X 120Hz dan baterai 5000 mAh.</p>
        <div class="modal-specs-grid">
            <div class="spec-item"><span class="spec-label">Processor</span><span class="spec-value">Snapdragon 8 Gen 2 for Galaxy</span></div>
            <div class="spec-item"><span class="spec-label">Layar</span><span class="spec-value">6.8" QHD+ (3088 x 1440), 120Hz</span></div>
            <div class="spec-item"><span class="spec-label">Kamera Belakang</span><span class="spec-value">200MP Utama, 12MP UW, 10MP Tele, 10MP Periskop</span></div>
            <div class="spec-item"><span class="spec-label">Kamera Depan</span><span class="spec-value">12MP Dual Pixel AF</span></div>
            <div class="spec-item"><span class="spec-label">Baterai</span><span class="spec-value">5000 mAh (45W Fast Charging)</span></div>
            <div class="spec-item"><span class="spec-label">Ketahanan</span><span class="spec-value">IP68, Armor Aluminum, Victus 2</span></div>
        </div>
    `,
    'iPhone 14': `
        <p class="modal-desc-intro">iPhone 14 menghadirkan performa mulus dengan chip A15 Bionic dan sistem kamera ganda yang ditingkatkan untuk foto low-light yang luar biasa.</p>
        <div class="modal-specs-grid">
            <div class="spec-item"><span class="spec-label">Layar</span><span class="spec-value">6.1" Super Retina XDR OLED</span></div>
            <div class="spec-item"><span class="spec-label">Chipset</span><span class="spec-value">A15 Bionic (5-core GPU)</span></div>
            <div class="spec-item"><span class="spec-label">Kamera</span><span class="spec-value">12MP Utama + 12MP Ultra Wide</span></div>
            <div class="spec-item"><span class="spec-label">Ketahanan</span><span class="spec-value">Ceramic Shield, IP68</span></div>
        </div>
    `,
    'Asus ROG': `
        <p class="modal-desc-intro">Laptop gaming ultimate dengan performa rata kanan. Dilengkapi pendingin mutakhir untuk sesi gaming berat.</p>
        <div class="modal-specs-grid">
            <div class="spec-item"><span class="spec-label">Layar</span><span class="spec-value">15.6" QHD 165Hz/3ms</span></div>
            <div class="spec-item"><span class="spec-label">Prosesor</span><span class="spec-value">Intel Core i9 Gen 13</span></div>
            <div class="spec-item"><span class="spec-label">Grafis</span><span class="spec-value">NVIDIA GeForce RTX 4070</span></div>
            <div class="spec-item"><span class="spec-label">Memori</span><span class="spec-value">32GB DDR5 / 1TB NVMe Gen4</span></div>
        </div>
    `,
    'Lenovo Legion': `
        <p class="modal-desc-intro">Desain elegan nan mematikan. Lenovo Legion memadukan gaya profesional dengan tenaga gaming hardcore.</p>
        <div class="modal-specs-grid">
            <div class="spec-item"><span class="spec-label">Layar</span><span class="spec-value">16" WQXGA 165Hz IPS</span></div>
            <div class="spec-item"><span class="spec-label">Prosesor</span><span class="spec-value">AMD Ryzen 7 7840HS</span></div>
            <div class="spec-item"><span class="spec-label">Grafis</span><span class="spec-value">NVIDIA RTX 4060 8GB</span></div>
            <div class="spec-item"><span class="spec-label">Memori</span><span class="spec-value">16GB DDR5 / 512GB SSD</span></div>
        </div>
    `,
    'iPad Pro': `
        <p class="modal-desc-intro">Kekuatan chip M2 dalam form factor tablet paling canggih di dunia. Cocok untuk profesional kreatif.</p>
        <div class="modal-specs-grid">
            <div class="spec-item"><span class="spec-label">Layar</span><span class="spec-value">11" Liquid Retina (ProMotion 120Hz)</span></div>
            <div class="spec-item"><span class="spec-label">Chipset</span><span class="spec-value">Apple M2 (8 CPU, 10 GPU)</span></div>
            <div class="spec-item"><span class="spec-label">Kamera</span><span class="spec-value">12MP Wide + 10MP Ultra Wide</span></div>
            <div class="spec-item"><span class="spec-label">Dukungan</span><span class="spec-value">Apple Pencil Gen 2, Magic Keyboard</span></div>
        </div>
    `,
    'default': `
        <p class="modal-desc-intro">Memiliki efisiensi daya yang luar biasa, teknologi layar jernih anti-flicker, serta arsitektur chipset generasi terbaru untuk menjamin kelancaran aktivitas harian.</p>
        <div class="modal-specs-grid">
            <div class="spec-item"><span class="spec-label">Kualitas</span><span class="spec-value">Premium Standard</span></div>
            <div class="spec-item"><span class="spec-label">Garansi</span><span class="spec-value">Resmi 1 Tahun</span></div>
        </div>
    `
};

const CATEGORY_FALLBACKS = {
    'Hp': 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=600&q=80',
    'Laptop': 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=600&q=80',
    'Tablet': 'https://images.unsplash.com/photo-1585776245991-cf89dd7fc73a?auto=format&fit=crop&w=600&q=80',
    'Tv': 'https://images.unsplash.com/photo-1593305841991-05c297ba4575?auto=format&fit=crop&w=600&q=80',
    'Ac': 'https://images.unsplash.com/photo-1621905251189-08b45d6a269e?auto=format&fit=crop&w=600&q=80',
    'Blender': 'https://images.unsplash.com/photo-1578643463396-0997cb5328c1?auto=format&fit=crop&w=600&q=80',
    'Kulkas': 'https://images.unsplash.com/photo-1584622650111-993a426fbf0a?auto=format&fit=crop&w=600&q=80'
};

function initializeProductImages() {
    const cards = document.querySelectorAll('.product-card');
    cards.forEach(card => {
        const name = card.dataset.nama;
        const category = card.dataset.kategori;
        const img = card.querySelector('.card-image-area img');
        if (img) {
            let src = PRODUCT_IMAGES[name];
            if (!src) {
                src = CATEGORY_FALLBACKS[category] || 'https://images.unsplash.com/photo-1526738549149-8e07eca6c147?auto=format&fit=crop&w=600&q=80';
            }
            img.src = src;
        }
        if (category) {
            card.classList.add(`cat-${category.toLowerCase()}`);
        }
    });
}

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
            if (inCartBadge) {
                inCartBadge.remove();
            }
            
            if (btnAdd) {
                btnAdd.innerHTML = `
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"></path><line x1="3" y1="6" x2="21" y2="6"></line><path d="M16 10a4 4 0 0 1-8 0"></path></svg>
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
            btnAddModal.onclick = () => {
                addToCartFromModal();
            };
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    initializeProductImages();
    renderCartCount();
    renderCart();
    setupSearchAndFilters();
    updateCartStates();
});

// FILTERING SYSTEM
function setupSearchAndFilters() {
    const searchInput = document.getElementById('searchInput');
    const chips = document.querySelectorAll('.chip');
    const sortSelect = document.getElementById('sortSelect');

    const runFilters = () => {
        const query = searchInput.value.toLowerCase().trim();
        const activeChip = document.querySelector('.chip.active');
        const category = activeChip ? activeChip.dataset.category : 'all';
        const sortOrder = sortSelect.value;

        let cards = Array.from(document.querySelectorAll('.product-card'));
        let visibleCount = 0;

        cards.forEach(card => {
            const name = card.dataset.nama.toLowerCase();
            const brand = card.dataset.merk.toLowerCase();
            const cate = card.dataset.kategori;

            const matchesSearch = name.includes(query) || brand.includes(query);
            const matchesCategory = (category === 'all' || cate === category);

            if (matchesSearch && matchesCategory) {
                card.style.display = 'flex';
                visibleCount++;
            } else {
                card.style.display = 'none';
            }
        });

        // Sorting Logic
        const grid = document.getElementById('productGrid');
        if (sortOrder !== 'default') {
            cards.sort((a, b) => {
                const priceA = parseFloat(a.dataset.harga);
                const priceB = parseFloat(b.dataset.harga);
                return sortOrder === 'low-high' ? priceA - priceB : priceB - priceA;
            });
            cards.forEach(card => grid.appendChild(card));
        }
        document.getElementById('countValue').textContent = visibleCount;
    };

    if (searchInput) searchInput.addEventListener('input', runFilters);
    chips.forEach(chip => {
        chip.addEventListener('click', () => {
            chips.forEach(c => c.classList.remove('active'));
            chip.classList.add('active');
            runFilters();
        });
    });
    if (sortSelect) sortSelect.addEventListener('change', runFilters);
}

// SHOW DETAIL MODAL
function showDetail(cardElement) {
    const id = cardElement.dataset.id;
    const nama = cardElement.dataset.nama;
    const merk = cardElement.dataset.merk;
    const kategori = cardElement.dataset.kategori;
    const harga = parseFloat(cardElement.dataset.harga);
    const stok = parseInt(cardElement.dataset.stok);
    const imgSrc = cardElement.querySelector('.card-image-area img').src;

    currentModal = { id, nama, merk, kategori, harga, stok, imgSrc };
    modalQty = 1;

    document.getElementById('modalNama').textContent = nama;
    document.getElementById('modalMerk').textContent = merk;
    document.getElementById('modalKategori').textContent = kategori;
    document.getElementById('modalHarga').textContent = formatRp(harga);
    document.getElementById('modalStok').textContent = `Stok: ${stok} unit`;
    document.getElementById('modalImg').src = imgSrc;
    
    // Set Deskripsi Spesifikasi Spesifik
    const descElement = document.getElementById('modalDesc');
    if (descElement) {
        descElement.innerHTML = PRODUCT_DESCRIPTIONS[nama] || PRODUCT_DESCRIPTIONS['default'];
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

// CART ACTIONS
function toggleCart() {
    document.getElementById('cartSidebar').classList.toggle('open');
    const overlay = document.getElementById('cartOverlay');
    overlay.classList.toggle('show');
}

function addToCartDirect(cardElement) {
    const id = cardElement.dataset.id;
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