
export let interval;

window.idleTime = 0;

export function init() {
    //interval = setInterval(trackLogin, 1260000);

    let idleInterval = setInterval(timerIncrement, 60000);

    $(document).mousemove(function (e) {

        idleTime = 0;
    });

    $(document).keypress(function (e) {
        idleTime = 0
    });


}

/*export function trackLogin() {
    var xmlReq = false;
    try {
        xmlReq = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
        try {
            xmlReq = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (e2) {
            xmlReq = false;
        }
    }
    if (!xmlReq && typeof XMLHttpRequest != 'undefined') {
        xmlReq = new XMLHttpRequest();
    }

    xmlReq.open('get', '../php/seguridad.php', true);
    //xmlReq.setRequestHeader("Connection", "close");
    xmlReq.send(null);
    xmlReq.onreadystatechange = function () {
        if (xmlReq.readyState == 4 && xmlReq.status == 200) {
            if (xmlReq.responseText == 1) {
                //console.log(xmlReq.responseText);
                clearInterval(interval);
                //alert('You have been logged out.You will now be redirected to home page.');
                $.messager.alert('Sesion', 'Tiempo de Sesion Terminado, Vuelve a Ingresar', 'info', function () {
                    location.href = '../index.php';
                });
                //document.location.href = "index.html";
            }
        }
    }
}*/


function timerIncrement(){

    idleTime = idleTime + 1;


    if (idleTime > 20){

        $.messager.alert('session', 'Tiempo de session agotado ' , 'info');

        $.ajax({

            url : '../php/logout.php',
            success: function (data) {},
            error: function (xhr, param1, param2) {}

        });

        setTimeout(function () {
            window.location.href = '../index.php';
        }, 2000);

        //window.location.reload();

    }
    window.location.href("https://www.google.com.ar/?gws_rd=ssl")


}
