<!DOCTYPE html>
<html>
<head>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
</head>
<body>
<h2>Java/Spring concurrency test on session</h2>
<p>Run with:
    <a href="?nosync">no sync</a> |
    sync on:
    <a href="?instance">instance</a> |
    <a href="?autowired">autowired</a> |
    <a href="?sessionmutex">sessionmutex</a>
</p>

<script>
(function() {
    $.ajaxSetup ({
        cache: false
    });

    var endpoint = "/api/cars?responseDelay=5000",
        parallellRequests = 3;

    if(window.location.search.length > 0) {
        endpoint += "&mode=" + window.location.search.substring(1);

        $.ajax({
          type: "POST",
          url: "/api/session",
          dataType: "json"
        }).done(function() {

            $.when.apply(this, (function() {
                var requests = [];
                for(var i = 0; i < parallellRequests; i++) {
                    requests.push(createRequest(i+1));
                }
                return requests;
            })()).done(function() {
                $.ajax({
                  type: "DELETE",
                  url: "/api/session",
                  dataType: "json"
                });
            });
        }).fail(function(xhr, errorText, errorThrown) {
            debugger;
        });
    }

    function createRequest(id) {
        $("body").append("<p>Request " + id + " starting...</p>");
        return $.getJSON(endpoint, function(data) {
          $("body").append("<p>Request " + id + " complete</p>");
      });

    }
})();

</script>
</body>
</html>
