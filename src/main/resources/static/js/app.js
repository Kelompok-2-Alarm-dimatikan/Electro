// PROFILE DROPDOWN
function toggleProfile() {
    document.getElementById('profileWrap').classList.toggle('open');
}
document.addEventListener('click', function(e) {
    const wrap = document.getElementById('profileWrap');
    if (wrap && !wrap.contains(e.target)) {
        wrap.classList.remove('open');
    }
});

// SLIDER
let currentSlide = 0;
const totalSlides = 3;
let autoSlideTimer;

function updateSlider() {
    const vp = document.getElementById('sliderViewport');
    if (!vp) return;
    const slidePercentage = 100 / totalSlides; 
    vp.style.transform = `translateX(-${currentSlide * slidePercentage}%)`;
    document.querySelectorAll('.dot').forEach((d, i) => {
        d.classList.toggle('active', i === currentSlide);
    });
}

function changeSlide(dir) {
    currentSlide = (currentSlide + dir + totalSlides) % totalSlides;
    updateSlider();
    resetAutoSlide();
}

function goToSlide(i) {
    currentSlide = i;
    updateSlider();
    resetAutoSlide();
}

function resetAutoSlide() {
    clearInterval(autoSlideTimer);
    autoSlideTimer = setInterval(() => changeSlide(1), 5000);
}

// Inisialisasi Slider 
document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('sliderViewport')) {
        updateSlider();
        resetAutoSlide();
        let touchStartX = 0;
        const sliderEl = document.querySelector('.slider-wrapper'); 
        if (sliderEl) {
            sliderEl.addEventListener('touchstart', e => { touchStartX = e.touches[0].clientX; }, {passive: true});
            sliderEl.addEventListener('touchend', e => {
                const diff = touchStartX - e.changedTouches[0].clientX;
                if (Math.abs(diff) > 50) changeSlide(diff > 0 ? 1 : -1);
            });
        }
    }
});

// BANNER KATEGORI — simpan filter ke sessionStorage saat klik banner
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.banner-btn[data-cat]').forEach(link => {
        link.addEventListener('click', () => {
            sessionStorage.setItem('filterCat', link.dataset.cat);
        });
    });
});


// CART HOME PAGE 
function toggleCart() {
    const sidebar = document.getElementById('cartSidebar');
    const overlay = document.getElementById('cartOverlay');
    if (!sidebar || !overlay) return;
    sidebar.classList.toggle('open');
    overlay.classList.toggle('show');
}

function renderCartHome() {
    const cart = JSON.parse(localStorage.getItem('electro_cart') || '[]');
    const count = cart.reduce((sum, item) => sum + item.qty, 0);
    const countEl = document.getElementById('cartCount');
    if (countEl) countEl.textContent = count;

    const container = document.getElementById('cartItems');
    if (!container) return;

    if (cart.length === 0) {
        container.innerHTML = `
            <div class="cart-empty-state">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="color:#3f3f46;margin-bottom:1rem"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/></svg>
                <p style="color:#52525b;font-size:0.9rem">Keranjang masih kosong</p>
                <a href="/product" style="color:#a1a1aa;font-size:0.8rem;margin-top:0.5rem;display:block">Lihat produk →</a>
            </div>`;
        const totalEl = document.getElementById('cartTotal');
        if (totalEl) totalEl.textContent = 'Rp 0';
        return;
    }

    let total = 0;
    container.innerHTML = '';
    cart.forEach((item, index) => {
        total += item.price * item.qty;
        container.innerHTML += `
            <div class="cart-item-row">
                <div class="cart-item-info">
                    <div class="cart-item-name">${item.name}</div>
                    <div class="cart-item-meta">${item.qty} x ${formatRpHome(item.price)}</div>
                </div>
                <button class="btn-remove-item" onclick="removeFromCartHome(${index})">Hapus</button>
            </div>`;
    });
    const totalEl = document.getElementById('cartTotal');
    if (totalEl) totalEl.textContent = formatRpHome(total);
}

function removeFromCartHome(index) {
    const cart = JSON.parse(localStorage.getItem('electro_cart') || '[]');
    cart.splice(index, 1);
    localStorage.setItem('electro_cart', JSON.stringify(cart));
    renderCartHome();
}

function checkout() {
    const cart = JSON.parse(localStorage.getItem('electro_cart') || '[]');
    if (cart.length === 0) return;
    window.location.href = '/checkout';
}

function formatRpHome(num) {
    return 'Rp ' + Math.round(num).toLocaleString('id-ID');
}

document.addEventListener('DOMContentLoaded', renderCartHome);