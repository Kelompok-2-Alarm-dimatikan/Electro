document.addEventListener('DOMContentLoaded', () => {

// NAVIGASI SIDEBAR + HEADER TITLE
const labels = { dashboard:'Dashboard', users:'Manajemen User', table:'Tabel Produk', complaints:'Manajemen Pengaduan' };
const navItems    = document.querySelectorAll('.nav-item');
const pages       = document.querySelectorAll('.page');
const headerTitle = document.getElementById('header-title');

let salesChartInstance = null;

const CHART_DATA = {
  labels: ['Jan','Feb','Mar','Apr','Mei','Jun','Jul','Agt','Sep','Okt','Nov','Des'],
  datasets: [
    { label:'HP',      data:[12,18,15,22,19,25,28,20,18,24,26,30], borderColor:'rgba(79,142,247,1)',  backgroundColor:'rgba(79,142,247,0.08)',  fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(79,142,247,1)' },
    { label:'Laptop',  data:[8,10,9,12,11,14,16,15,13,17,19,21],   borderColor:'rgba(247,196,79,1)',  backgroundColor:'rgba(247,196,79,0.08)',  fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(247,196,79,1)' },
    { label:'Tablet',  data:[4,5,6,7,5,8,9,8,6,10,12,14],          borderColor:'rgba(79,199,138,1)',  backgroundColor:'rgba(79,199,138,0.08)', fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(79,199,138,1)' },
    { label:'Blender', data:[6,7,5,9,8,10,11,10,8,12,14,16],        borderColor:'rgba(247,101,79,1)',  backgroundColor:'rgba(247,101,79,0.08)', fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(247,101,79,1)' },
    { label:'Kulkas',  data:[3,4,5,4,6,5,7,6,5,8,10,12],            borderColor:'rgba(160,120,255,1)', backgroundColor:'rgba(160,120,255,0.08)',fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(160,120,255,1)' },
    { label:'AC',      data:[5,6,7,8,9,10,11,12,13,14,15,16],       borderColor:'rgba(79,220,220,1)',  backgroundColor:'rgba(79,220,220,0.08)', fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(79,220,220,1)' },
    { label:'TV',      data:[10,12,14,16,18,20,22,24,26,28,30,32],  borderColor:'rgba(247,160,79,1)',  backgroundColor:'rgba(247,160,79,0.08)', fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(247,160,79,1)' },
    { label:'Headphone', data:[7,8,9,10,11,12,13,14,15,16,17,18], borderColor:'rgba(79,199,138,1)',  backgroundColor:'rgba(79,199,138,0.08)', fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(79,199,138,1)' }
  ]
};

const CHART_OPTIONS = {
  responsive: true,
  maintainAspectRatio: false,
  interaction:{ mode:'index', intersect:false },
  plugins:{
    legend:{
      labels:{ color:'#7b82a0', font:{family:'DM Sans',size:12}, usePointStyle:true, pointStyle:'circle' }
    }
  },
  scales:{
    x:{ ticks:{color:'#7b82a0'}, grid:{color:'rgba(42,47,69,0.4)'} },
    y:{ ticks:{color:'#7b82a0', callback: v => 'Rp '+v+'jt'}, grid:{color:'rgba(42,47,69,0.4)'}, beginAtZero:true }
  }
};

function buildChart() {
  const canvas = document.getElementById('salesChart');
  if (!canvas) return;
  if (salesChartInstance) {
    salesChartInstance.resize();
    return;
  }
  
  const wrap = canvas.parentElement;
  if (!wrap || wrap.offsetWidth === 0) return;
  canvas.width  = wrap.offsetWidth;
  canvas.height = 260;
  salesChartInstance = new Chart(canvas.getContext('2d'), {
    type: 'line',
    data: CHART_DATA,
    options: CHART_OPTIONS
  });
}

// Pakai ResizeObserver pada chart-wrap 
const chartWrap = document.querySelector('.chart-wrap');
if (chartWrap && typeof ResizeObserver !== 'undefined') {
  const ro = new ResizeObserver(entries => {
    for (const entry of entries) {
      if (entry.contentRect.width > 0) {
        buildChart();
        if (salesChartInstance) ro.disconnect();
      }
    }
  });
  ro.observe(chartWrap);
}

function switchPage(pg) {
  navItems.forEach(i => i.classList.remove('active'));
  pages.forEach(p => p.classList.remove('active'));
  const targetNav = document.querySelector(`.nav-item[data-page="${pg}"]`);
  const targetPage = document.getElementById('page-' + pg);
  if (targetNav) targetNav.classList.add('active');
  if (targetPage) targetPage.classList.add('active');
  if (headerTitle) headerTitle.textContent = labels[pg] || pg;
  closeDropdown();

  if (pg === 'dashboard') {
    // rAF memastikan DOM sudah paint sebelum kita ukur
    requestAnimationFrame(() => requestAnimationFrame(buildChart));
  }
}

navItems.forEach(item => {
  item.addEventListener('click', () => {
    switchPage(item.dataset.page);
  });
});

document.getElementById('toggle-btn').addEventListener('click', () => {
  document.body.classList.toggle('sidebar-closed');
  if (salesChartInstance) {
    setTimeout(() => salesChartInstance.resize(), 350);
  }
});

// Inisialisasi saat halaman pertama load dengan double rAF
requestAnimationFrame(() => requestAnimationFrame(buildChart));

// STOCK LIST 
const stockData = [
  {label:'HP',count:451,color:'#4f8ef7'},{label:'Laptop',count:84,color:'#f7c44f'},
  {label:'Tablet',count:104,color:'#4fc78a'},{label:'Blender',count:562,color:'#f7654f'},
  {label:'Kulkas',count:264,color:'#a078ff'},{label:'AC',count:150,color:'#4fdcdc'},
  {label:'TV',count:320,color:'#f7a04f'},{label:'Headphone',count:200,color:'#4fc78a'},
];
const maxStock = Math.max(...stockData.map(s => s.count));
const stockListEl = document.getElementById('stockList');
if (stockListEl) {
  stockData.forEach(s => {
    const pct = Math.round(s.count / maxStock * 100);
    stockListEl.innerHTML += `<div class="stock-item">
      <div class="stock-dot" style="background:${s.color}"></div>
      <div class="stock-name">${s.label}</div>
      <div class="stock-bar-wrap"><div class="stock-bar" style="width:${pct}%;background:${s.color}"></div></div>
      <div class="stock-num">${s.count}</div>
    </div>`;
  });
}

// POPULAR TABLE 
const products = [
  {nama:'Philips Pro Blender',kat:'Blender',terjual:185,harga:1299000,stok:210},
  {nama:'Miyako Turbo Blender',kat:'Blender',terjual:162,harga:499000,stok:185},
  {nama:'Samsung S23 Ultra',kat:'Hp',terjual:143,harga:14999000,stok:10},
  {nama:'Xiaomi 13',kat:'Hp',terjual:98,harga:8999000,stok:10},
  {nama:'iPhone 14',kat:'Hp',terjual:89,harga:13999000,stok:10},
];
function katBadge(k) {
  const m={Hp:'badge-hp',Laptop:'badge-laptop',Tablet:'badge-tablet',Blender:'badge-blender',Kulkas:'badge-kulkas',Tv:'badge-tv',Ac:'badge-ac'};
  return `<span class="badge ${m[k]||'badge-hp'}">${k}</span>`;
}
function stokBadge(s) {
  if(s===0) return `<span class="stock-badge stock-out">● Habis</span>`;
  if(s<10)  return `<span class="stock-badge stock-low">● Rendah</span>`;
  return `<span class="stock-badge stock-ok">● Tersedia</span>`;
}
const popTbody = document.getElementById('popularTable');
if (popTbody) {
  products.forEach((p,i) => {
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
}

// SEARCH & FILTER PRODUK — hanya cocokkan kolom Nama Produk (td ke-3, index 2)
const searchInput = document.getElementById('searchInput');
if (searchInput) {
  searchInput.addEventListener('input', e => {
    const q = e.target.value.toLowerCase().trim();
    document.querySelectorAll('#productTable tr').forEach(row => {
      // td index 2 = kolom Nama Produk
      const namaCell = row.querySelectorAll('td')[2];
      const nama = namaCell ? namaCell.textContent.toLowerCase() : '';
      row.style.display = (q === '' || nama.includes(q)) ? '' : 'none';
    });
  });
}

// SEARCH USER — Staff/Admin table dan Customer table
const userSearchInput = document.getElementById('userSearchInput');
if (userSearchInput) {
  userSearchInput.addEventListener('input', e => {
    const q = e.target.value.toLowerCase().trim();
    // Cari di semua user-table tbody tr
    document.querySelectorAll('.user-table tbody tr').forEach(row => {
      const username = row.querySelectorAll('td')[1]?.textContent.toLowerCase() || '';
      const email    = row.querySelectorAll('td')[2]?.textContent.toLowerCase() || '';
      row.style.display = (q === '' || username.includes(q) || email.includes(q)) ? '' : 'none';
    });
  });
}

// SEARCH COMPLAINTS
const complaintSearchInput = document.getElementById('complaintSearchInput');
if (complaintSearchInput) {
  complaintSearchInput.addEventListener('input', e => {
    const q = e.target.value.toLowerCase().trim();
    document.querySelectorAll('#complaintsTable tbody tr').forEach(row => {
      const pelapor  = row.querySelectorAll('td')[1]?.textContent.toLowerCase() || '';
      const kategori = row.querySelectorAll('td')[2]?.textContent.toLowerCase() || '';
      const detail   = row.querySelectorAll('td')[4]?.textContent.toLowerCase() || '';
      row.style.display = (q === '' || pelapor.includes(q) || kategori.includes(q) || detail.includes(q)) ? '' : 'none';
    });
  });
}

const filterGroup = document.getElementById('filterGroup');
if (filterGroup) {
  filterGroup.addEventListener('click', e => {
    const btn = e.target.closest('.filter-btn');
    if (!btn) return;
    document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    const cat = btn.dataset.cat;
    document.querySelectorAll('#productTable tr').forEach(row => {
      const rowCat = row.dataset.kategori || '';
      row.style.display = (cat === 'all' || rowCat === cat) ? '' : 'none';
    });
  });
}

// DROPDOWN AKSI PRODUK
let openDropId = null;
function closeDropdown() {
  if (openDropId !== null) {
    document.getElementById('drop-' + openDropId)?.classList.remove('show');
    document.querySelector(`.action-btn[data-pid="${openDropId}"]`)?.classList.remove('is-open');
    openDropId = null;
  }
}

const productTable = document.getElementById('productTable');
if (productTable) {
  productTable.addEventListener('click', e => {
    const btn = e.target.closest('.action-btn');
    if (btn) {
      e.stopPropagation();
      const pid  = btn.dataset.pid;
      const drop = document.getElementById('drop-' + pid);
      if (openDropId === pid) { closeDropdown(); return; }
      closeDropdown();
      const rect = btn.getBoundingClientRect();
      if (window.innerHeight - rect.bottom < 200) drop.classList.add('drop-up');
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
      const action = item.dataset.action;
      if (action === 'edit') {
        openEditProduk(item);
      } else {
        openModal(action, item.dataset.pid, item.dataset.nama, parseInt(item.dataset.stok));
      }
    }
  });
}
document.addEventListener('click', closeDropdown);

// MODAL STOK / HAPUS
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
let activePid=null, activeAction=null, activeStok=0, activeNama='';

const ICON_PATHS = {
  add:    '<line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>',
  reduce: '<line x1="5" y1="12" x2="19" y2="12"/>',
  delete: '<polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/><path d="M10 11v6M14 11v6"/><path d="M9 6V4h6v2"/>',
};

function updateAfterPreview() {
  if (activeAction==='delete') return;
  const qty = Math.max(1, parseInt(qtyInput.value)||1);
  const after = activeAction==='add' ? activeStok+qty : Math.max(0, activeStok-qty);
  mStokAfter.textContent = after;
  mStokAfter.className = 'msb-val '+(after===0?'red':after<10?'yellow':'green');
}

function openModal(action, pid, nama, stok) {
  activePid=pid; activeAction=action; activeStok=stok; activeNama=nama;
  mIconWrap.className = 'modal-icon-wrap icon-'+action;
  mIconSvg.innerHTML  = ICON_PATHS[action];
  mProduct.textContent = nama;
  if (action==='add') {
    mTitle.textContent='Tambah Stok'; mSubtitle.textContent='Masukkan jumlah unit yang ingin ditambahkan.';
    mQtyLabel.textContent='JUMLAH TAMBAHAN'; mBtnOk.className='btn-ok add'; mBtnOk.textContent='Tambah Stok';
    mStokRow.style.display=mQtySection.style.display='';
    mStokNow.textContent=stok; mStokNow.className='msb-val'; qtyInput.value=1; updateAfterPreview();
  } else if (action==='reduce') {
    mTitle.textContent='Kurangi Stok'; mSubtitle.textContent='Masukkan jumlah unit yang ingin dikurangi.';
    mQtyLabel.textContent='JUMLAH PENGURANGAN'; mBtnOk.className='btn-ok reduce'; mBtnOk.textContent='Kurangi Stok';
    mStokRow.style.display=mQtySection.style.display='';
    mStokNow.textContent=stok; mStokNow.className='msb-val'; qtyInput.value=1; updateAfterPreview();
  } else {
    mTitle.textContent='Hapus Produk?'; mSubtitle.textContent='Produk akan dihapus permanen. Tidak bisa dibatalkan.';
    mBtnOk.className='btn-ok delete'; mBtnOk.textContent='Ya, Hapus Produk';
    mStokRow.style.display=mQtySection.style.display='none';
  }
  overlay.classList.add('show');
}

function closeModal() { overlay.classList.remove('show'); activePid=activeAction=null; }

document.getElementById('qtyMinus').addEventListener('click', () => { qtyInput.value=Math.max(1,parseInt(qtyInput.value)-1); updateAfterPreview(); });
document.getElementById('qtyPlus').addEventListener('click', () => {
  const max = activeAction==='reduce' ? activeStok : 9999;
  qtyInput.value=Math.min(max,parseInt(qtyInput.value)+1); updateAfterPreview();
});
qtyInput.addEventListener('input', updateAfterPreview);
document.getElementById('mBtnCancel').addEventListener('click', closeModal);
overlay.addEventListener('click', e => { if(e.target===overlay) closeModal(); });

mBtnOk.addEventListener('click', () => {
  if (!activePid) return;
  const qty = Math.max(1, parseInt(qtyInput.value)||1);
  if (activeAction==='add') {
    submitForm('/admin/tambahStok', {id:activePid, jumlah:qty});
  } else if (activeAction==='reduce') {
    if (qty>activeStok) { showToast('Stok tidak mencukupi!','#f7654f'); closeModal(); return; }
    submitForm('/admin/kurangiStok', {id:activePid, jumlah:qty});
  } else {
    submitForm('/admin/hapus/'+activePid, {}, 'GET');
  }
  closeModal();
});

// MODAL TAMBAH / EDIT PRODUK
const produkOverlay   = document.getElementById('produkModalOverlay');
const produkForm      = document.getElementById('produkForm');
const produkTitle     = document.getElementById('produkModalTitle');
const produkFormId    = document.getElementById('produkFormId');

const btnTambahProduk = document.getElementById('btnTambahProduk');
if (btnTambahProduk) {
  btnTambahProduk.addEventListener('click', () => {
    produkTitle.textContent = 'Tambah Produk';
    produkForm.action = '/admin/tambah';
    produkFormId.value = '';
    produkForm.reset();
    produkOverlay.classList.add('show');
  });
}

function openEditProduk(item) {
  produkTitle.textContent = 'Edit Produk';
  produkForm.action = '/admin/edit';
  produkFormId.value = item.dataset.pid;

  document.getElementById('pNama').value        = item.dataset.nama       || '';
  document.getElementById('pMerk').value        = item.dataset.merk       || '';
  document.getElementById('pHarga').value       = item.dataset.harga      || '';
  document.getElementById('pStok').value        = item.dataset.stok       || '0';
  document.getElementById('pImageUrl').value    = item.dataset.imageurl   || '';
  document.getElementById('pDeskripsi').value   = item.dataset.deskripsi  || '';
  document.getElementById('pSpesifikasi').value = item.dataset.spesifikasi|| '';

  const sel = document.getElementById('pKategori');
  for (let opt of sel.options) {
    if (opt.value === item.dataset.kategori) { opt.selected=true; break; }
  }
  produkOverlay.classList.add('show');
}

function closeProdukModal() { produkOverlay.classList.remove('show'); }
document.getElementById('closeProdukModal').addEventListener('click', closeProdukModal);
document.getElementById('closeProdukModal2').addEventListener('click', closeProdukModal);
produkOverlay.addEventListener('click', e => { if(e.target===produkOverlay) closeProdukModal(); });

// PASSWORD TOGGLE - User Modal
const toggleUserPw = document.getElementById('toggleUserPw');
if (toggleUserPw) {
  toggleUserPw.addEventListener('click', () => {
    const input   = document.getElementById('uPassword');
    const eyeOn   = document.getElementById('eyeIcon');
    const eyeOff  = document.getElementById('eyeOffIcon');
    const isHidden = input.type === 'password';
    input.type = isHidden ? 'text' : 'password';
    eyeOn.style.display  = isHidden ? 'none'  : '';
    eyeOff.style.display = isHidden ? ''      : 'none';
  });
}

// MODAL TAMBAH / EDIT USER
const userOverlay = document.getElementById('userModalOverlay');
const userForm    = document.getElementById('userForm');
const userTitle   = document.getElementById('userModalTitle');
const userFormId  = document.getElementById('userFormId');

const btnTambahUser = document.getElementById('btnTambahUser');
if (btnTambahUser) {
  btnTambahUser.addEventListener('click', () => {
    userTitle.textContent = 'Tambah User';
    userForm.action = '/admin/user/tambah';
    userFormId.value = '';
    userForm.reset();
    document.getElementById('uPasswordField').style.display = '';
    document.getElementById('uPassword').required = true;
    userOverlay.classList.add('show');
  });
}

window.openEditUser = function(btn) {
  userTitle.textContent = 'Edit User';
  userForm.action = '/admin/user/edit';
  userFormId.value              = btn.dataset.id;
  document.getElementById('uUsername').value = btn.dataset.username || '';
  document.getElementById('uEmail').value    = btn.dataset.email    || '';
  document.getElementById('uRole').value     = btn.dataset.role     || 'USER';
  // sembunyikan field password saat edit
  document.getElementById('uPasswordField').style.display = 'none';
  document.getElementById('uPassword').required = false;
  userOverlay.classList.add('show');
};

function closeUserModal() { userOverlay.classList.remove('show'); }
document.getElementById('closeUserModal').addEventListener('click', closeUserModal);
document.getElementById('closeUserModal2').addEventListener('click', closeUserModal);
userOverlay.addEventListener('click', e => { if(e.target===userOverlay) closeUserModal(); });

// HELPER SUBMIT FORM
function submitForm(action, params, method='POST') {
  const form = document.createElement('form');
  form.method = method==='GET' ? 'GET' : 'POST';
  form.action = action;
  const csrfMeta = document.querySelector('meta[name="_csrf"]');
  if (csrfMeta && method!=='GET') {
    const csrf = document.createElement('input');
    csrf.type='hidden'; csrf.name='_csrf'; csrf.value=csrfMeta.content;
    form.appendChild(csrf);
  }
  Object.entries(params).forEach(([k,v]) => {
    const inp = document.createElement('input');
    inp.type='hidden'; inp.name=k; inp.value=v;
    form.appendChild(inp);
  });
  document.body.appendChild(form);
  form.submit();
}

// TOAST
let toastTimer=null;
function showToast(msg, color='#4fc78a') {
  const toast = document.getElementById('toast');
  document.getElementById('tDot').style.background = color;
  document.getElementById('tMsg').textContent = msg;
  toast.classList.add('show');
  clearTimeout(toastTimer);
  toastTimer = setTimeout(()=>toast.classList.remove('show'), 3000);
}

}); // end DOMContentLoaded

// PROFILE PANEL 
(function() {
  const wrapper = document.getElementById('profileWrapper');
  const btn     = document.getElementById('profileBtn');
  if (!wrapper || !btn) return;

  btn.addEventListener('click', e => {
    e.stopPropagation();
    wrapper.classList.toggle('open');
  });
  document.addEventListener('click', e => {
    if (!wrapper.contains(e.target)) wrapper.classList.remove('open');
  });
  document.addEventListener('keydown', e => {
    if (e.key==='Escape') wrapper.classList.remove('open');
  });

  const pwOverlay = document.getElementById('pwModalOverlay');
  const btnOpenPw = document.getElementById('btnOpenPw');
  const btnClose1 = document.getElementById('btnClosePw');
  const btnClose2 = document.getElementById('btnClosePw2');

  function openPwModal()  { wrapper.classList.remove('open'); if(pwOverlay) pwOverlay.classList.add('active'); }
  function closePwModal() { if(pwOverlay) pwOverlay.classList.remove('active'); }

  if (btnOpenPw) btnOpenPw.addEventListener('click', openPwModal);
  if (btnClose1) btnClose1.addEventListener('click', closePwModal);
  if (btnClose2) btnClose2.addEventListener('click', closePwModal);
  if (pwOverlay) pwOverlay.addEventListener('click', e => { if(e.target===pwOverlay) closePwModal(); });

  const hasFlash = pwOverlay && (pwOverlay.querySelector('.pw-alert-error') || pwOverlay.querySelector('.pw-alert-success'));
  if (hasFlash) openPwModal();

    // SEARCH PENGIRIMAN
  const orderSearchInput = document.getElementById('orderSearchInput');
  if (orderSearchInput) {
    orderSearchInput.addEventListener('input', function () {
      const q = this.value.toLowerCase();
      document.querySelectorAll('#ordersTable tbody tr').forEach(row => {
        row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
      });
    });
  }

})();