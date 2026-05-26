document.addEventListener("DOMContentLoaded", () => {
    const bgPattern = document.querySelector(".bg-pattern");
    if (!bgPattern) return;

    // stars
    const starsContainer = document.createElement("div");
    starsContainer.className = "stars-container";
    document.body.appendChild(starsContainer);

    // Number of stars
    const numDotStars = 100;
    const numSparkleStars = 20;

    // dot stars
    for (let i = 0; i < numDotStars; i++) {
        const dot = document.createElement("div");
        dot.className = "dot-star";
        const size = Math.random() * 2 + 1; 
        dot.style.width = `${size}px`;
        dot.style.height = `${size}px`;
        dot.style.left = `${Math.random() * 100}%`;
        dot.style.top = `${Math.random() * 100}%`;
        dot.style.opacity = Math.random() * 0.6 + 0.3;
        
        // Randomize horizontal drift direction
        dot.style.setProperty('--drift-x', (Math.random() * 200 - 100));

        // Twinkle and drift upwards
        dot.style.animation = `twinkleStar ${Math.random() * 4 + 3}s infinite ease-in-out alternate, driftUp ${Math.random() * 20 + 20}s infinite linear`;
        dot.style.animationDelay = `${Math.random() * 5}s, ${Math.random() * 10}s`;
        starsContainer.appendChild(dot);
    }

    // sparkle stars 
    for (let i = 0; i < numSparkleStars; i++) {
        const sparkle = document.createElement("div");
        sparkle.className = "sparkle-star-wrap";
        
        // sizes
        const size = Math.random() * 14 + 12; 
        sparkle.style.width = `${size}px`;
        sparkle.style.height = `${size}px`;
        sparkle.style.left = `${Math.random() * 100}%`;
        sparkle.style.top = `${Math.random() * 100}%`;
        sparkle.innerHTML = `
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style="width:100%; height:100%;">
                <path d="M12 0L14.8 9.2L24 12L14.8 14.8L12 24L9.2 14.8L0 12L9.2 9.2L12 0Z" fill="white"/>
            </svg>
        `;
        
        sparkle.style.setProperty('--drift-x', (Math.random() * 300 - 150));
        
        // Twinkling, breathing and rotating animation
        const duration = Math.random() * 5 + 5; // 5s to 10s
        sparkle.style.animation = `shimmerStar ${duration}s infinite ease-in-out, driftUp ${Math.random() * 20 + 20}s infinite linear`;
        sparkle.style.animationDelay = `${Math.random() * 6}s, ${Math.random() * 10}s`;
        starsContainer.appendChild(sparkle);
    }

    // Shooting stars
    const numShootingStars = 5;
    for (let i = 0; i < numShootingStars; i++) {
        createShootingStar();
    }

    function createShootingStar() {
        const shootingStar = document.createElement("div");
        shootingStar.className = "shooting-star";
        shootingStar.style.left = `${Math.random() * 80}%`;
        shootingStar.style.top = `${Math.random() * 40}%`;
        
        // shooting down-right
        const angle = Math.random() * 40 + 20; 
        shootingStar.style.setProperty('--angle', `${angle}deg`);
        
        const duration = Math.random() * 3 + 2;
        shootingStar.style.animation = `shoot ${duration}s infinite linear`;
        shootingStar.style.animationDelay = `${Math.random() * 10}s`;
        
        starsContainer.appendChild(shootingStar);
    }
});
