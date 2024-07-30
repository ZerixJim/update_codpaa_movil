/*$(document).ready(function () {





 });*/

function RecoverPass() {
    var usuario_pass = $('#usuario_us').val();

    if (usuario_pass != "") {

        $.ajax({
            type: 'POST',
            url: './php/recover_pass.php',
            data: {usuario_pass: usuario_pass},

            success: function (data) {
                if (data == '1') {
                    alert('Se envio una nueva contraseña a su email registrado.');

                    $('#dlg_pass').dialog('close');
                }
                else if (data == '2') {
                    alert('El usuario proporcioando no existe o no tiene email registrado.');
                }
                else if (data == '0') {
                    alert('Ocurrio un Problema');
                }
            },

            error: function (jqXHR, textStatus, error) {

                alert("error: " + jqXHR.responseText);
            }
        });

    }
    else {
        alert('Favor de Escribir su nombre de usuario');
    }
}