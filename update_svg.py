import re
import os

html_files = [
    r'c:\Users\syafi\OneDrive\Pictures\Documents\New folder (2)\clone\New folder\New folder\Electro\src\main\resources\templates\settings.html',
    r'c:\Users\syafi\OneDrive\Pictures\Documents\New folder (2)\clone\New folder\New folder\Electro\src\main\resources\templates\login.html',
    r'c:\Users\syafi\OneDrive\Pictures\Documents\New folder (2)\clone\New folder\New folder\Electro\src\main\resources\templates\register.html'
]

closed_eye_path = '<path d="M2 8c4 4 16 4 20 0" /><path d="M12 11v4" /><path d="M8 9.5l-2 3" /><path d="M16 9.5l2 3" />'

for file_path in html_files:
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    pattern = r'<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"\s*/>\s*<circle cx="12" cy="12" r="3"\s*/>'
    content = re.sub(pattern, closed_eye_path, content)
    
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)

js_files = {
    r'c:\Users\syafi\OneDrive\Pictures\Documents\New folder (2)\clone\New folder\New folder\Electro\src\main\resources\static\js\login.js': '''function togglePassword(inputId, btn) {
            const input = document.getElementById(inputId);
            const svg = btn.querySelector('svg');
            if (input.type === 'password') {
                input.type = 'text';
                if (svg) svg.innerHTML = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>';
                btn.style.opacity = '1';
            } else {
                input.type = 'password';
                if (svg) svg.innerHTML = '<path d="M2 8c4 4 16 4 20 0" /><path d="M12 11v4" /><path d="M8 9.5l-2 3" /><path d="M16 9.5l2 3" />';
                btn.style.opacity = '0.5';
            }
        }''',
    r'c:\Users\syafi\OneDrive\Pictures\Documents\New folder (2)\clone\New folder\New folder\Electro\src\main\resources\static\js\register.js': '''function toggleBothPasswords(btn) {
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
        }'''
}

for fp, new_content in js_files.items():
    with open(fp, 'w', encoding='utf-8') as f:
        f.write(new_content)

settings_js_path = r'c:\Users\syafi\OneDrive\Pictures\Documents\New folder (2)\clone\New folder\New folder\Electro\src\main\resources\static\js\settings.js'
with open(settings_js_path, 'r', encoding='utf-8') as f:
    settings_content = f.read()

settings_func = '''        function togglePassword(inputId, button) {
            const input = document.getElementById(inputId);
            const svg = button.querySelector('svg');
            if (!input) return;
            
            if (input.type === 'password') {
                input.type = 'text';
                if (svg) svg.innerHTML = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>';
                button.classList.add('visible');
            } else {
                input.type = 'password';
                if (svg) svg.innerHTML = '<path d="M2 8c4 4 16 4 20 0" /><path d="M12 11v4" /><path d="M8 9.5l-2 3" /><path d="M16 9.5l2 3" />';
                button.classList.remove('visible');
            }
        }'''

settings_content = re.sub(
    r'function togglePassword\(inputId,\s*button\)\s*\{.*?\n\s*\}\n',
    settings_func + '\n',
    settings_content,
    flags=re.DOTALL
)

with open(settings_js_path, 'w', encoding='utf-8') as f:
    f.write(settings_content)

print('Done updating icons and JS')
