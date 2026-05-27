function toggleBothPasswords(btn) {
            const pw = document.getElementById('passwordInput');
            const cpw = document.getElementById('confirmPasswordInput');
            const isHidden = pw.type === 'password';
            pw.type = isHidden ? 'text' : 'password';
            cpw.type = isHidden ? 'text' : 'password';
            btn.style.opacity = isHidden ? '1' : '0.5';
        }