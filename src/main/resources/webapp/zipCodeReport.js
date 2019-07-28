(function () {
    $(document).ready(function(){
        $.get('http://localhost:8080/report/mostFrequentZipCodes?recordNumber=10', function(data) {
            data.forEach(function (item, index) {
               var markup = "<tr><td>" + item.zipCode + "</td><td>" + item.count + "</td></tr>";
               $("table tbody").append(markup);
            });
        });
    });
})();
