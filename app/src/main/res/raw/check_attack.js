(function() {
    var attacks = [];
    var flightFederation = document.getElementsByClassName('flight federation');
    var flightAttack = document.getElementsByClassName('flight attack');
    if (flightFederation.length > 0 || flightAttack.length > 0) {
        for(var i = 0; i < flightFederation.length; i++) {
            const row = flightFederation[0];
            attacks.push(row.innerText);
        }
        for(var i = 0; i < flightAttack.length; i++) {
            const row = flightAttack[0];
            attacks.push(row.innerText);
        }
    }
    return attacks.join('\n');
})();
//document.getElementsByClassName('flight attack')[0].parentElement.previousElementSibling.getElementsByClassName("z")[0].id