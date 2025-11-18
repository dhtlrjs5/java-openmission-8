// File: src/main/resources/templates/wonder.html (JavaScript)
const GIF_DURATION = 4000;

// 💡 희귀 아이템 목록 및 색상 정의
const RARE_ITEMS = ["크림", "샤샤", "팬디"];
const RARE_COLOR = "#DAA520"; // 골드 색상
const NORMAL_COLOR = "white"; // 일반 색상

/**
 * 💡 뽑힌 아이템을 중앙에 표시하고 순차적인 이펙트를 적용합니다.
 */
function showDrawEffect(item) {
    if (!item || !item.name) return;

    const modal = document.getElementById("draw-effect-modal");
    const gif = document.getElementById("effect-stage1-gif");
    const revealStage = document.getElementById("effect-stage2-reveal");
    const itemNameElement = document.getElementById("effect-item-name");
    const itemImageElement = document.getElementById("effect-item-image");

    // 1. 아이템 이름과 이미지 경로 설정
    itemNameElement.textContent = item.name;

    // 💡 색상 동적 설정
    if (RARE_ITEMS.includes(item.name)) {
        itemNameElement.style.color = RARE_COLOR;
    } else {
        itemNameElement.style.color = NORMAL_COLOR;
    }

    // 🚨 이미지 경로 동적 생성: 아이템 이름(공백 제거)을 기반으로 경로를 생성합니다.
    const cleanName = item.name.replace(/ /g, "");
    const imagePath = "/pet/" + cleanName + ".gif";
    itemImageElement.src = imagePath;

    // 2. 초기 설정 및 모달 표시
    revealStage.style.display = 'none';
    gif.style.display = 'block';
    modal.style.display = 'flex';

    // 3. 타이머 1: GIF 재생 후 결과 이미지 표시
    setTimeout(() => {
        gif.style.display = 'none';
        revealStage.style.display = 'block';
    }, GIF_DURATION);
}

/**
 * 통계 및 결과 화면을 업데이트하는 함수
 */
function updateStatsView(data) {
    const stats = data.statistics;
    const resultItem = data.item;

    // 1. 이펙트 출력 (1회 사용 시)
    if (resultItem) {
        showDrawEffect(resultItem);
    }

    // 2. Top Statistics Update
    document.getElementById("stat-count").textContent = stats.count;
    document.getElementById("stat-cost").textContent = stats.cost.toLocaleString('ko-KR');

    // 3. Result Item Update
    document.getElementById("result-item").textContent = resultItem ? resultItem.name : "-";

    // 4. Individual Item Counts Update
    for (const [name, count] of Object.entries(stats.itemCount)) {
        const id = "item-" + name.replace(/ /g, "");
        const element = document.getElementById(id);
        if (element) {
            element.textContent = count;
        }
    }
}

/**
 * 화면 통계를 0으로 초기화하는 함수
 */
function resetView() {
    document.getElementById("stat-count").textContent = 0;
    document.getElementById("stat-cost").textContent = 0;
    document.getElementById("result-item").textContent = "-";

    const itemIds = [
        "item-크림", "item-샤샤", "item-팬디",
        "item-고농축프리미엄생명의물", "item-오가닉원더쿠키",
        "item-식빵이", "item-빵둘기", "item-제키", "item-제나", "item-제리"
    ];

    itemIds.forEach(id => {
        const element = document.getElementById(id);
        if (element) {
            element.textContent = 0;
        }
    });
}

// --- Event Handlers ---

document.getElementById("use-btn").addEventListener("click", async () => {
    const res = await fetch("/wonder/use");
    if (res.ok) {
        const data = await res.json();
        updateStatsView(data);
    } else {
        alert("1회 사용 중 오류 발생: " + res.status);
    }
});

document.getElementById("use10-btn").addEventListener("click", async () => {
    const res = await fetch("/wonder/use10");
    if (res.ok) {
        const data = await res.json();
        updateStatsView(data);
    } else {
        alert("10회 사용 중 오류 발생: " + res.status);
    }
});

document.getElementById("reset-btn").addEventListener("click", async () => {
    const res = await fetch("/wonder/reset");
    if (res.ok) {
        const data = await res.json();
        resetView();
        updateStatsView(data);
    } else {
        alert("초기화 중 오류 발생: " + res.status);
    }
});

// 💡 닫기 버튼 핸들러 (새로 추가)
document.getElementById("close-effect-btn").addEventListener("click", () => {
    document.getElementById("draw-effect-modal").style.display = 'none';
});

// 초기 로드 시 뷰를 0으로 설정
resetView();