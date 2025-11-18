let equipment = {level: 250, star: 0, price: 0, destroyed: false};
window.starStatistics = {totalCost: 0, attempts: 0, destruction: 0};

const updateStatistics = (stats) => {
    window.starStatistics = stats ?? {totalCost: 0, attempts: 0, destruction: 0};
};

const handleResponse = async (res) => {
    if (!res.ok) {
        alert(`API 오류 발생: ${res.status}`);
        return;
    }

    const responseData = await res.json();

    const resultEquipment = responseData.equipment || responseData;
    const resultStatistics = responseData.statistics || window.starStatistics;

    if (!resultEquipment || resultEquipment.star === undefined) {
        console.error("Invalid response format: equipment data missing or invalid.", responseData);
        alert("서버 응답 형식이 올바르지 않아 장비 정보를 업데이트할 수 없습니다.");
        return;
    }

    equipment.star = resultEquipment.star;
    equipment.price = resultEquipment.price.toLocaleString('ko-KR');
    equipment.destroyed = resultEquipment.destroyed;

    updateStatistics(resultStatistics);
    updateView();
};


const updateView = () => {
    document.getElementById("star-count").textContent = equipment.star;
    document.getElementById("equipment-price").textContent = equipment.price.toLocaleString('ko-KR');
    document.getElementById("total-cost").textContent = window.starStatistics.totalCost.toLocaleString('ko-KR');
    document.getElementById("attempts").textContent = window.starStatistics.attempts;
    document.getElementById("destruction").textContent = window.starStatistics.destruction;

    const enhanceBtn = document.getElementById("enhance-btn");
    const repairBtn = document.getElementById("repair-btn");

    enhanceBtn.disabled = equipment.destroyed;
    repairBtn.disabled = !equipment.destroyed;

    if (equipment.destroyed) {
        enhanceBtn.classList.add('disabled');
        repairBtn.classList.remove('disabled');
    } else {
        enhanceBtn.classList.remove('disabled');
        repairBtn.classList.add('disabled');
    }
};

document.getElementById("set-price-btn").addEventListener("click", async () => {
    let price = Number(document.getElementById("set-price-input").value);
    if (isNaN(price) || price < 0) price = 0;
    equipment.price = price;

    const res = await fetch("/star/set-price", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(equipment)
    });

    if (!res.ok) {
        alert(`API 오류 발생: ${res.status}`);
        return;
    }

    const responseData = await res.json();
    const resultEquipment = responseData.equipment || responseData;
    const resultStatistics = responseData.statistics || window.starStatistics;

    if (!resultEquipment || resultEquipment.star === undefined) {
        console.error("Invalid set-price response format.", responseData);
        alert("가격 설정 후 서버 응답 형식이 올바르지 않습니다.");
        return;
    }

    equipment = resultEquipment;
    updateStatistics(resultStatistics);
    updateView();
});

document.getElementById("enhance-btn").addEventListener("click", async () => {
    const res = await fetch("/star/enhance", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(equipment)
    });
    await handleResponse(res);
});

document.getElementById("repair-btn").addEventListener("click", async () => {
    const res = await fetch("/star/repair", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(equipment)
    });
    await handleResponse(res);
});

document.getElementById("reset-btn").addEventListener("click", async () => {

    const res = await fetch("/star/reset");

    if (!res.ok) {
        alert(`서버 초기화 실패: ${res.status}`);
        return;
    }

    const responseData = await res.json();

    const resultEquipment = responseData.equipment || responseData;
    const resultStatistics = responseData.statistics || {totalCost: 0, attempts: 0, destruction: 0};

    equipment.star = resultEquipment.star || 0;
    equipment.price = resultEquipment.price || 0;
    equipment.destroyed = resultEquipment.destroyed || false;
    equipment.level = 250; // 레벨은 하드코딩된 값으로 유지

    window.starStatistics = resultStatistics;

    updateView();
});

updateView();