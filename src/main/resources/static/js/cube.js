const limitMap = {
    DEFAULT_RARE: 10,
    DEFAULT_EPIC: 42,
    DEFAULT_UNIQUE: 102,
    ADDITIONAL_RARE: 31,
    ADDITIONAL_EPIC: 76,
    ADDITIONAL_UNIQUE: 214,
    LEGENDARY: 0
};

async function rollCube(url, tableId, gradeId, countId) {
    const res = await fetch(url);
    const data = await res.json();
    const potential = data.potential;
    const statistics = data.statistics;

    // 등급 업데이트
    document.getElementById(gradeId).textContent = potential.grade;

    // limitMap 키 조합: 기본/에디셔널 + 등급
    const typePrefix = potential.type === 'ADDITIONAL' ? 'ADDITIONAL_' : 'DEFAULT_';
    const limitKey = typePrefix + potential.grade;
    const limit = limitMap[limitKey] || 0;

    // 천장 카운트 업데이트
    document.getElementById(countId).textContent =
        `(${limit === 0 ? '-' : potential.count} / ${limit === 0 ? '-' : limit})`;

    // 옵션 테이블 초기화
    const table = document.getElementById(tableId);
    table.innerHTML = tableId === 'default-table'
        ? '<tr><th>잠재옵션</th></tr>'
        : '<tr><th>에디셔널 잠재옵션</th></tr>';

    // 옵션 추가
    potential.options.forEach(opt => {
        const row = table.insertRow();
        const cell = row.insertCell();
        cell.textContent = opt.name;
    });

    if (statistics) {
        document.getElementById('stat-default-count').textContent = statistics.defaultCount;
        document.getElementById('stat-additional-count').textContent = statistics.additionalCount;
        document.getElementById('stat-default-cost').textContent = statistics.defaultCost.toLocaleString('ko-KR');
        document.getElementById('stat-additional-cost').textContent = statistics.additionalCost.toLocaleString('ko-KR');
        document.getElementById('stat-total-cost').textContent = statistics.totalCost.toLocaleString('ko-KR');
    }
}

document.getElementById('default-btn').addEventListener('click', () => {
    rollCube('/cube/use', 'default-table', 'grade', 'default-count');
});

document.getElementById('additional-btn').addEventListener('click', () => {
    rollCube('/cube/use/additional', 'additional-table', 'additional-grade', 'additional-count');
});

document.getElementById('reset-btn').addEventListener('click', async () => {
    const res = await fetch('/cube/reset');
    const data = await res.json();
    const potential = data.potential;
    const statistics = data.statistics;

    // 등급 초기화
    document.getElementById('grade').textContent = potential.grade;
    document.getElementById('additional-grade').textContent = potential.grade;

    // 천장 카운트 초기화
    document.getElementById('default-count').textContent = '(0 / 10)';
    document.getElementById('additional-count').textContent = '(0 / 31)';

    // 통계 초기화
    if (statistics) {
        document.getElementById('stat-default-count').textContent = statistics.defaultCount;
        document.getElementById('stat-additional-count').textContent = statistics.additionalCount;
        document.getElementById('stat-default-cost').textContent = statistics.defaultCost;
        document.getElementById('stat-additional-cost').textContent = statistics.additionalCost;
        document.getElementById('stat-total-cost').textContent = statistics.totalCost;
    }

    // 테이블 초기화
    ['default-table', 'additional-table'].forEach(id => {
        const table = document.getElementById(id);
        table.innerHTML = id === 'default-table'
            ? '<tr><th>잠재옵션</th></tr>'
            : '<tr><th>에디셔널 잠재옵션</th></tr>';

        for (let i = 0; i < 3; i++) {
            const row = table.insertRow();
            const cell = row.insertCell();
            cell.textContent = '-';
        }
    });
});