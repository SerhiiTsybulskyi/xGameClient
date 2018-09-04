(function() {
    var attacks = [];
    var flightFederation = document.getElementsByClassName('flight federation');
    var flightAttack = document.getElementsByClassName('flight attack');
    if (flightFederation.length > 0 || flightAttack.length > 0) {
        for(var i = 0; i < flightFederation.length; i++) {
            var descriptionRow = flightFederation[0];
            var timeRow = descriptionRow.parentElement.previousElementSibling;
            var attackId = timeRow.getElementsByClassName("z")[0].id;
            var countDown = timeRow.getElementsByClassName("z")[0].innerText;
            var dateTime = timeRow.getElementsByClassName("z")[0].nextElementSibling.innerText
            attacks.push({
                attackId: attackId,
                countDown: countDown,
                dateTime: dateTime
            });
        }
        for(var i = 0; i < flightAttack.length; i++) {
            var descriptionRow = flightAttack[0];
            var timeRow = descriptionRow.parentElement.previousElementSibling;
            var attackId = timeRow.getElementsByClassName("z")[0].id;
            var countDown = timeRow.getElementsByClassName("z")[0].innerText;
            var dateTime = timeRow.getElementsByClassName("z")[0].nextElementSibling.innerText
            attacks.push({
                attackId: attackId,
                countDown: countDown,
                dateTime: dateTime
            });
        }
    }

    return "{ " + JSON.stringify(attacks) + " }";
})();
