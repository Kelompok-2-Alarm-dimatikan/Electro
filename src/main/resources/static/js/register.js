function toggleBothPasswords(btn) {
            const pw = document.getElementById('passwordInput');
            const cpw = document.getElementById('confirmPasswordInput');
            const svg = btn.querySelector('svg');
            const isHidden = pw.type === 'password';
            pw.type = isHidden ? 'text' : 'password';
            if (cpw) cpw.type = isHidden ? 'text' : 'password';
            if (svg) {
                svg.innerHTML = isHidden 
                    ? '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>'
                    : '<path d="M2 8c4 4 16 4 20 0" /><path d="M12 11v4" /><path d="M8 9.5l-2 3" /><path d="M16 9.5l2 3" />';
            }
            btn.style.opacity = isHidden ? '1' : '0.5';
        }