// CHECKOUT PAGE LOGIC

let cart = [];
const SERVICE_FEE = 10000;

document.addEventListener('DOMContentLoaded', () => {
    loadCartData();
    renderSummary();
});

function loadCartData() {
    try {
        cart = JSON.parse(localStorage.getItem('electro_cart') || '[]');
    } catch (e) {
        cart = [];
    }
}

function renderSummary() {
    const container = document.getElementById('summaryItems');
    const subtotalEl = document.getElementById('subtotalCost');
    const totalEl = document.getElementById('totalCost');
    const btnPlaceOrder = document.getElementById('btnPlaceOrder');

    if (!container) return;

    if (cart.length === 0) {
        container.innerHTML = `
            <div class="empty-checkout-cart" style="text-align:center; padding: 2rem 0; color: var(--text-muted);">
                <span style="font-size: 2.5rem; display:block; margin-bottom:0.8rem;">🛒</span>
                Keranjang belanja Anda kosong.
            </div>
        `;
        subtotalEl.textContent = 'Rp 0';
        totalEl.textContent = 'Rp 0';
        if (btnPlaceOrder) {
            btnPlaceOrder.disabled = true;
            btnPlaceOrder.textContent = 'Keranjang Kosong';
            btnPlaceOrder.style.opacity = '0.5';
            btnPlaceOrder.style.cursor = 'not-allowed';
        }
        return;
    }

    container.innerHTML = '';
    let subtotal = 0;

    cart.forEach(item => {
        const itemTotal = item.price * item.qty;
        subtotal += itemTotal;
        
        container.innerHTML += `
            <div class="checkout-item-row">
                <div class="item-info-col">
                    <strong>${item.name}</strong>
                    <span>Jumlah: ${item.qty} unit × ${formatRp(item.price)}</span>
                </div>
                <div class="item-price-col">
                    ${formatRp(itemTotal)}
                </div>
            </div>
        `;
    });

    const total = subtotal + SERVICE_FEE;
    subtotalEl.textContent = formatRp(subtotal);
    totalEl.textContent = formatRp(total);
}

async function submitOrder() {
    const btnPlaceOrder = document.getElementById('btnPlaceOrder');
    const address = document.getElementById('shippingAddress').value.trim();
    const phone = document.getElementById('phoneNumber').value.trim();
    const paymentMethodEl = document.querySelector('input[name="paymentMethod"]:checked');
    
    if (cart.length === 0) {
        showToast('Keranjang Anda kosong!', '#f87171');
        return;
    }

    if (!address || !phone || !paymentMethodEl) {
        showToast('Harap lengkapi semua formulir pengiriman!', '#f87171');
        return;
    }

    const paymentMethod = paymentMethodEl.value;

    // Get CSRF Token
    const csrfMeta = document.querySelector('meta[name="_csrf"]');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
    const headers = {
        'Content-Type': 'application/json'
    };

    if (csrfMeta && csrfHeader) {
        headers[csrfHeader.content] = csrfMeta.content;
    }

    // Prepare payload
    const payload = {
    items: cart,
    address: document.getElementById('shippingAddress').value,
    paymentMethod: document.querySelector('input[name="paymentMethod"]:checked').value,
    fullName: document.getElementById('fullName').value,       
    email:    document.getElementById('emailAddr').value,      
    phone:    document.getElementById('phoneNumber').value     
    };

    // UI Loading State
    const originalBtnText = btnPlaceOrder.textContent;
    btnPlaceOrder.disabled = true;
    btnPlaceOrder.textContent = 'Memproses Pesanan Anda...';
    btnPlaceOrder.style.opacity = '0.7';

    try {
        const response = await fetch('/checkout/submit', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(payload)
        });

        const data = await response.json();

        if (response.ok && data.success) {
            // Show Success Modal
            const subtotal = cart.reduce((sum, item) => sum + (item.price * item.qty), 0);
            const total = subtotal + SERVICE_FEE;
            
            document.getElementById('successPaymentMethod').textContent = getFriendlyPaymentName(paymentMethod);
            document.getElementById('successTotalCost').textContent = formatRp(total);
            
            // Clear cart from local storage
            localStorage.removeItem('electro_cart');
            cart = [];
            
            document.getElementById('successOverlay').classList.add('show');
        } else {
            showToast(data.message || 'Gagal memproses pesanan.', '#f87171');
            btnPlaceOrder.disabled = false;
            btnPlaceOrder.textContent = originalBtnText;
            btnPlaceOrder.style.opacity = '1';
        }
    } catch (error) {
        console.error('Error submitting checkout:', error);
        showToast('Terjadi kesalahan koneksi ke server.', '#f87171');
        btnPlaceOrder.disabled = false;
        btnPlaceOrder.textContent = originalBtnText;
        btnPlaceOrder.style.opacity = '1';
    }
}

function getFriendlyPaymentName(method) {
    switch (method) {
        case 'BANK_TRANSFER': return '🏦 Transfer Bank (Virtual Account)';
        case 'E_WALLET': return '📱 Dompet Digital / E-Wallet';
        default: return method;
    }
}

function finishCheckout() {
    window.location.href = '/product';
}

function formatRp(num) {
    return 'Rp ' + Math.round(num).toLocaleString('id-ID');
}

function showToast(msg, color = '#22c55e') {
    const el = document.getElementById('toast');
    if (!el) return;
    el.textContent = msg;
    el.style.borderLeft = `4px solid ${color}`;
    el.classList.add('show');
    setTimeout(() => el.classList.remove('show'), 3000);
}