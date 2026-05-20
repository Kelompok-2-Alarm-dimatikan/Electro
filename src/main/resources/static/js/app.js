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
        const sliderEl = document.querySelector('.slider-wrapper'); // Perbaikan nama class
        if (sliderEl) {
            sliderEl.addEventListener('touchstart', e => { touchStartX = e.touches[0].clientX; }, {passive: true});
            sliderEl.addEventListener('touchend', e => {
                const diff = touchStartX - e.changedTouches[0].clientX;
                if (Math.abs(diff) > 50) changeSlide(diff > 0 ? 1 : -1);
            });
        }
    }
});