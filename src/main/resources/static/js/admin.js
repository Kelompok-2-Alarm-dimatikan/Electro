document.addEventListener('DOMContentLoaded', () => {

const labels = { dashboard:'Dashboard', users:'Users', table:'Tabel Produk' };
const navItems = document.querySelectorAll('.nav-item');
const pages    = document.querySelectorAll('.page');
const headerTitle = document.getElementById('headerTitle');

navItems.forEach(item => {
  item.addEventListener('click', () => {
    const pg = item.dataset.page;
    navItems.forEach(i => i.classList.remove('active'));
    pages.forEach(p => p.classList.remove('active'));
    item.classList.add('active');
    document.getElementById('page-' + pg).classList.add('active');
    headerTitle.textContent = labels[pg];
    closeDropdown();
  });
});

document.getElementById('toggle-btn').addEventListener('click', () => {
  document.body.classList.toggle('sidebar-closed');
});

// ── CHART ─────────────────────────────────────────────────────────────────────
// ── CHART ─────────────────────────────────────────────────────────────────────
const ctx = document.getElementById('salesChart').getContext('2d');

const myChart = new Chart(ctx, {
  type: 'line',
  data: {
    labels: ['Jan','Feb','Mar','Apr','Mei','Jun','Jul', 'Agt', 'Sep','Okt', 'Nov', 'Des'],
    datasets: [
      {
        label: 'HP',
        data: [12,18,15,22,19,25,28, 20, 18, 24, 26, 30],
        borderColor: 'rgba(79,142,247,1)',
        backgroundColor: 'rgba(79,142,247,0.08)',
        fill: true,
        tension: 0.4,
        borderWidth: 2,
        pointStyle: 'circle', 
        pointRadius: 4,      
        pointHoverRadius: 6,
        pointBackgroundColor: 'rgba(79,142,247,1)' 
      },
      {
        label: 'Laptop',
        data: [8,10,9,12,11,14,16, 15, 13, 17, 19, 21],
        borderColor: 'rgba(247,196,79,1)',
        backgroundColor: 'rgba(247,196,79,0.08)',
        fill: true,
        tension: 0.4,
        borderWidth: 2,
        pointStyle: 'circle',
        pointRadius: 4,
        pointHoverRadius: 6,
        pointBackgroundColor: 'rgba(247,196,79,1)'
      },
      {
        label: 'Tablet',
        data: [4,5,6,7,5,8,9, 8, 6, 10, 12, 14],
        borderColor: 'rgba(79,199,138,1)',
        backgroundColor: 'rgba(79,199,138,0.08)',
        fill: true,
        tension: 0.4,
        borderWidth: 2,
        pointStyle: 'circle',
        pointRadius: 4,
        pointHoverRadius: 6,
        pointBackgroundColor: 'rgba(79,199,138,1)'
      },
      {
        label: 'Blender',
        data: [6,7,5,9,8,10,11, 10, 8, 12, 14, 16],
        borderColor: 'rgba(247,101,79,1)',
        backgroundColor: 'rgba(247,101,79,0.08)',
        fill: true,
        tension: 0.4,
        borderWidth: 2,
        pointStyle: 'circle',
        pointRadius: 4,
        pointHoverRadius: 6,
        pointBackgroundColor: 'rgba(247,101,79,1)'
      },
      {
        label: 'Kulkas',
        data: [3,4,5,4,6,5,7, 6, 5, 8, 10, 12],
        borderColor: 'rgba(160,120,255,1)',
        backgroundColor: 'rgba(160,120,255,0.08)',
        fill: true,
        tension: 0.4,
        borderWidth: 2,
        pointStyle: 'circle',
        pointRadius: 4,
        pointHoverRadius: 6,
        pointBackgroundColor: 'rgba(160,120,255,1)'
      },
      {
        label: 'Ac',
        data: [5,6,7,8,9,10,11, 12, 13, 14, 15, 16],
        borderColor: 'rgba(79,142,247,1)',
        backgroundColor: 'rgba(79,142,247,0.08)',
        fill: true,
        tension: 0.4,
        borderWidth: 2,
        pointStyle: 'circle',
        pointRadius: 4,
        pointHoverRadius: 6,
        pointBackgroundColor: 'rgba(79,142,247,1)'
      },
      {
        label: 'Tv',
        data: [10,12,14,16,18,20,22, 24, 26, 28, 30, 32],
        borderColor: 'rgba(247,196,79,1)',
        backgroundColor: 'rgba(247,196,79,0.08)',
        fill: true,
        tension: 0.4,
        borderWidth: 2,
        pointStyle: 'circle',
        pointRadius: 4,
        pointHoverRadius: 6,
        pointBackgroundColor: 'rgba(247,196,79,1)'
      },
    ]
  },
  options: {
    responsive: true,
    interaction: {
      mode: 'index',
      intersect: false
    },
    plugins: {
      legend: {
        labels: {
          color: '#7b82a0',
          font: { family: 'DM Sans', size: 12 },
          usePointStyle: true,
          pointStyle: 'circle',   
          generateLabels: function(chart) {
            const labels = Chart.defaults.plugins.legend.labels.generateLabels(chart);
            labels.forEach(label => {
                label.pointStyle = 'circle';
                label.fillStyle = chart.data.datasets[label.datasetIndex].borderColor;
                if (label.hidden) {
                    label.textDecoration = 'line-through';
                    label.fillStyle = 'rgba(123, 130, 160, 0.3)'; 
                } else {
                    label.textDecoration = 'none'; 
                }
            });
            return labels;
          }
        },
        onClick: function(e, legendItem, legend) {
            const index = legendItem.datasetIndex;
            const ci = legend.chart;
            if (ci.isDatasetVisible(index)) {
                ci.hide(index);
                legendItem.hidden = true;
            } else {
                ci.show(index);
                legendItem.hidden = false;
            }
            ci.update();
        }
      }
    },
    scales: {
      x: {
        ticks: { color: '#7b82a0' },
        grid: { color: 'rgba(42,47,69,0.4)', drawBorder: false }
      },
      y: {
        ticks: {
          color: '#7b82a0',
          callback: v => 'Rp ' + v + 'jt'
        },
        grid: { color: 'rgba(42,47,69,0.4)', drawBorder: false },
        beginAtZero: true
      }
    }
  }
});

// ── STOCK LIST ────────────────────────────────────────────────────────────────
const stockData = [
  { label:'HP',      count:451, color:'#4f8ef7' },
  { label:'Laptop',  count:84,  color:'#f7c44f' },
  { label:'Tablet',  count:104, color:'#4fc78a' },
  { label:'Blender', count:562, color:'#f7654f' },
  { label:'Kulkas',  count:264, color:'#a078ff' },
  { label:'Ac', count:150, color:'#4f8ef7' },
  { label:'Tv', count:320, color:'#f7c44f'},
  { label:'Headphone', count:200, color:'#4fc78a'},
];
const maxStock = Math.max(...stockData.map(s => s.count));
const stockList = document.getElementById('stockList');
stockData.forEach(s => {
  const pct = Math.round(s.count / maxStock * 100);
  stockList.innerHTML += `<div class="stock-item">
    <div class="stock-dot" style="background:${s.color}"></div>
    <div class="stock-name">${s.label}</div>
    <div class="stock-bar-wrap"><div class="stock-bar" style="width:${pct}%;background:${s.color}"></div></div>
    <div class="stock-num">${s.count}</div>
  </div>`;
});

// ── POPULAR TABLE ─────────────────────────────────────────────────────────────
const products = [
  { nama:'Philips HR2041', kat:'Blender', terjual:185, harga:350000, stok:210 },
  { nama:'Miyako BL-101 PF', kat:'Blender', terjual:162, harga:165000, stok:185 },
  { nama:'Samsung Galaxy A55', kat:'HP', terjual:143, harga:4299000, stok:87 },
  { nama:'Xiaomi Redmi Note 13', kat:'HP', terjual:98, harga:2499000, stok:112 },
  { nama:'Realme C67', kat:'HP', terjual:89, harga:1899000, stok:130 },
  { nama:'Panasonic MX-GM1011', kat:'Blender', terjual:78, harga:450000, stok:95 },
  { nama:'Oppo A98 5G', kat:'HP', terjual:76, harga:3299000, stok:54 },
  { nama:'Tefal BL2A0166', kat:'Blender', terjual:54, harga:620000, stok:60 },
];
function katBadge(k) {
  const m = { HP:'badge-hp', Laptop:'badge-laptop', Tablet:'badge-tablet', Blender:'badge-blender', Kulkas:'badge-kulkas' };
  return `<span class="badge ${m[k]||''}">${k}</span>`;
}
function stokBadge(s) {
  if (s === 0) return `<span class="stock-badge stock-out">● Habis</span>`;
  if (s < 20)  return `<span class="stock-badge stock-low">● Rendah</span>`;
  return `<span class="stock-badge stock-ok">● Tersedia</span>`;
}
const popTbody = document.getElementById('popularTable');
products.forEach((p, i) => {
  const rc = i===0?'gold':i===1?'silver':i===2?'bronze':'';
  popTbody.innerHTML += `<tr>
    <td><span class="rank-num ${rc}">${i+1}</span></td>
    <td><strong>${p.nama}</strong></td>
    <td>${katBadge(p.kat)}</td>
    <td><strong>${p.terjual}</strong> unit</td>
    <td class="price-cell">Rp ${(p.terjual * p.harga / 1e6).toFixed(1)}jt</td>
    <td>${stokBadge(p.stok)} <span style="color:var(--muted);font-size:12px;margin-left:4px">${p.stok}</span></td>
  </tr>`;
});

// ── USER GRID ─────────────────────────────────────────────────────────────────
const usersStatic = [
  { name:'Admin Utama',   email:'admin@electro.id',  role:'admin', initial:'A', color:'#4f8ef7' },
  { name:'Budi Santoso',  email:'budi@electro.id',   role:'staff', initial:'B', color:'#4fc78a' },
  { name:'Citra Dewi',    email:'citra@electro.id',  role:'staff', initial:'C', color:'#f7654f' },
  { name:'Deni Hermawan', email:'deni@electro.id',   role:'staff', initial:'D', color:'#f7c44f' },
  { name:'Eka Putri',     email:'eka@electro.id',    role:'staff', initial:'E', color:'#a078ff' },
  { name:'Fajar Rizki',   email:'fajar@electro.id',  role:'staff', initial:'F', color:'#4fc78a' },
];
const ugEl = document.getElementById('userGrid');
usersStatic.forEach(u => {
  ugEl.innerHTML += `<div class="user-card-big">
    <div class="ub-avatar" style="background:${u.color}22;color:${u.color}">${u.initial}</div>
    <div class="ub-name">${u.name}</div>
    <div class="ub-email">${u.email}</div>
    <span class="ub-role ${u.role==='admin'?'admin':''}">${u.role}</span>
  </div>`;
});

// ── SEARCH & FILTER 
document.getElementById('searchInput').addEventListener('input', e => {
  const q = e.target.value.toLowerCase();
  document.querySelectorAll('#productTable tr').forEach(row => {
    const text = row.textContent.toLowerCase();
    row.style.display = text.includes(q) ? '' : 'none';
  });
});
document.getElementById('filterGroup').addEventListener('click', e => {
  const btn = e.target.closest('.filter-btn');
  if (!btn) return;
  document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
  btn.classList.add('active');
  const cat = btn.dataset.cat;
  document.querySelectorAll('#productTable tr').forEach(row => {
    row.style.display = (cat === 'all' || row.textContent.includes(cat)) ? '' : 'none';
  });
});

// ── DROPDOWN ──────────────────────────────────────────────────────────────────
let openDropId = null;

function closeDropdown() {
  if (openDropId !== null) {
    document.getElementById('drop-' + openDropId)?.classList.remove('show');
    document.querySelector(`.action-btn[data-pid="${openDropId}"]`)?.classList.remove('is-open');
    openDropId = null;
  }
}

document.getElementById('productTable').addEventListener('click', e => {
  const btn = e.target.closest('.action-btn');
  if (btn) {
    e.stopPropagation();
    const pid  = btn.dataset.pid;
    const drop = document.getElementById('drop-' + pid);
    if (openDropId === pid) { closeDropdown(); return; }
    closeDropdown();
    const rect = btn.getBoundingClientRect();
    if (window.innerHeight - rect.bottom < 180) drop.classList.add('drop-up');
    else drop.classList.remove('drop-up');
    drop.classList.add('show');
    btn.classList.add('is-open');
    openDropId = pid;
    return;
  }
  const item = e.target.closest('.drop-item');
  if (item) {
    e.stopPropagation();
    closeDropdown();
    openModal(item.dataset.action, item.dataset.pid, item.dataset.nama, parseInt(item.dataset.stok));
  }
});
document.addEventListener('click', closeDropdown);

// ── MODAL ─────────────────────────────────────────────────────────────────────
const overlay     = document.getElementById('modalOverlay');
const mIconWrap   = document.getElementById('mIconWrap');
const mIconSvg    = document.getElementById('mIconSvg');
const mTitle      = document.getElementById('mTitle');
const mSubtitle   = document.getElementById('mSubtitle');
const mProduct    = document.getElementById('mProduct');
const mStokRow    = document.getElementById('mStokRow');
const mStokNow    = document.getElementById('mStokNow');
const mStokAfter  = document.getElementById('mStokAfter');
const mQtySection = document.getElementById('mQtySection');
const mQtyLabel   = document.getElementById('mQtyLabel');
const qtyInput    = document.getElementById('qtyInput');
const mBtnOk      = document.getElementById('mBtnOk');

let activePid = null, activeAction = null, activeStok = 0, activeNama = '';

const ICON_PATHS = {
  add:    '<line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>',
  reduce: '<line x1="5" y1="12" x2="19" y2="12"/>',
  delete: '<polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/><path d="M10 11v6M14 11v6"/><path d="M9 6V4h6v2"/>',
};

function updateAfterPreview() {
  if (activeAction === 'delete') return;
  const qty = Math.max(1, parseInt(qtyInput.value) || 1);
  const after = activeAction === 'add' ? activeStok + qty : Math.max(0, activeStok - qty);
  mStokAfter.textContent = after;
  mStokAfter.className = 'msb-val ' + (after === 0 ? 'red' : after < 20 ? 'yellow' : 'green');
}

function openModal(action, pid, nama, stok) {
  activePid = pid; activeAction = action; activeStok = stok; activeNama = nama;
  mIconWrap.className = 'modal-icon-wrap icon-' + action;
  mIconSvg.innerHTML  = ICON_PATHS[action];
  mProduct.textContent = nama;
  if (action === 'add') {
    mTitle.textContent    = 'Tambah Stok';
    mSubtitle.textContent = 'Masukkan jumlah unit yang ingin ditambahkan.';
    mQtyLabel.textContent = 'JUMLAH TAMBAHAN';
    mBtnOk.className = 'btn-ok add'; mBtnOk.textContent = 'Tambah Stok';
    mStokRow.style.display = mQtySection.style.display = '';
    mStokNow.textContent = stok; mStokNow.className = 'msb-val';
    qtyInput.value = 1; updateAfterPreview();
  } else if (action === 'reduce') {
    mTitle.textContent    = 'Kurangi Stok';
    mSubtitle.textContent = 'Masukkan jumlah unit yang ingin dikurangi.';
    mQtyLabel.textContent = 'JUMLAH PENGURANGAN';
    mBtnOk.className = 'btn-ok reduce'; mBtnOk.textContent = 'Kurangi Stok';
    mStokRow.style.display = mQtySection.style.display = '';
    mStokNow.textContent = stok; mStokNow.className = 'msb-val';
    qtyInput.value = 1; updateAfterPreview();
  } else {
    mTitle.textContent    = 'Hapus Produk?';
    mSubtitle.textContent = 'Produk akan dihapus permanen. Tidak bisa dibatalkan.';
    mBtnOk.className = 'btn-ok delete'; mBtnOk.textContent = 'Ya, Hapus Produk';
    mStokRow.style.display = mQtySection.style.display = 'none';
  }
  overlay.classList.add('show');
}

function closeModal() {
  overlay.classList.remove('show');
  activePid = activeAction = null;
}

document.getElementById('qtyMinus').addEventListener('click', () => {
  qtyInput.value = Math.max(1, parseInt(qtyInput.value) - 1);
  updateAfterPreview();
});
document.getElementById('qtyPlus').addEventListener('click', () => {
  const max = activeAction === 'reduce' ? activeStok : 9999;
  qtyInput.value = Math.min(max, parseInt(qtyInput.value) + 1);
  updateAfterPreview();
});
qtyInput.addEventListener('input', updateAfterPreview);
document.getElementById('mBtnCancel').addEventListener('click', closeModal);
overlay.addEventListener('click', e => { if (e.target === overlay) closeModal(); });

// Konfirmasi — kirim form ke Spring Boot backend
mBtnOk.addEventListener('click', () => {
  if (!activePid) return;
  const qty = Math.max(1, parseInt(qtyInput.value) || 1);

  if (activeAction === 'add') {
    submitForm('/admin/tambahStok', { id: activePid, jumlah: qty });
  } else if (activeAction === 'reduce') {
    if (qty > activeStok) { showToast('Stok tidak mencukupi!', '#f7654f'); closeModal(); return; }
    submitForm('/admin/kurangiStok', { id: activePid, jumlah: qty });
  } else {
    submitForm('/admin/hapus/' + activePid, {}, 'GET');
  }
  closeModal();
});

// Helper: kirim form ke backend
function submitForm(action, params, method = 'POST') {
  const form = document.createElement('form');
  form.method = method === 'GET' ? 'GET' : 'POST';
  form.action = action;
  // CSRF token 
  const csrfMeta  = document.querySelector('meta[name="_csrf"]');
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
  if (csrfMeta && method !== 'GET') {
    const csrf = document.createElement('input');
    csrf.type  = 'hidden';
    csrf.name  = '_csrf';
    csrf.value = csrfMeta.content;
    form.appendChild(csrf);
  }
  Object.entries(params).forEach(([k, v]) => {
    const inp = document.createElement('input');
    inp.type = 'hidden'; inp.name = k; inp.value = v;
    form.appendChild(inp);
  });
  document.body.appendChild(form);
  form.submit();
}

// ── TOAST ─────────────────────────────────────────────────────────────────────
let toastTimer = null;
function showToast(msg, color) {
  const toast = document.getElementById('toast');
  document.getElementById('tDot').style.background = color;
  document.getElementById('tMsg').textContent = msg;
  toast.classList.add('show');
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => toast.classList.remove('show'), 3000);
}
});