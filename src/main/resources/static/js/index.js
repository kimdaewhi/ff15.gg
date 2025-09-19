function searchSummonerInfo() {
    const input = document.getElementById("summoner_input").value.trim();

    if (!input.includes("#")) {
        alert("소환사명과 태그를 #으로 구분해서 입력해주세요.");
        return false; // 폼 제출 막기
    }

    const [name, tag] = input.split("#");
    console.log(`name : ${name}, tag : ${tag}`)
    const url = `/account/get-summoner-info/${encodeURIComponent(name)}/${encodeURIComponent(tag)}`;

    window.location.href = url; // 엔드포인트로 이동
    return false; // 기본 폼 제출 방지
}