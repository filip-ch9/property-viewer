function sendDeleteRequest(url , rUrl, token) {
    $.ajax({
        url: url,
        method: "DELETE",
        data:{
            csrfToken:token
        },
        success: function () {
            window.location =  rUrl;
        },
        error: function() {
            //window.location.reload();
        }
    });
}

document.getElementById("myBtn").addEventListener("click" ,sendDeleteRequest);

function sendDeleteRequest() {
    let that = this;
    $.ajax({
        url: that.getAttribute('data-url'), //Here
        method: "DELETE",
        success: function () {
            window.location = that.getAttribute('data-redirect-url'); //Here
        },
        error: function() {
            window.location.reload();
        }
    });
}


