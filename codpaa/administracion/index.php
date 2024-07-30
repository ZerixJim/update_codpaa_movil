<!doctype html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <title>Codpaa Web</title>
    <link rel="stylesheet" href="css/estilo2.css">
    <link rel="stylesheet" type="text/css" href="./complemento/themes/default/easyui.css">
    <link rel="stylesheet" href="./complemento/bootstrap/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="./complemento/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="./complemento/themes/colores.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="./complemento/bootstrap/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            crossorigin="anonymous"></script>
</head>

<body style="background: url('imagenes/mapBackgroundBlack2.jpg') repeat-y;background-size: cover;">

<!--    <img src="imagenes/logo.png">-->

<div class="container" style="background-color:transparent;height: 100vh;">

    <div class="row justify-content-center align-items-center" style="height: 100%">

        <div class="col-xs-12 col-sm-10 col-md-11 col-lg-8" style="background-color: #FFFFFF;position: absolute;">
            <div class="row">
                <div class="col-0 col-md-4">
                    <div class="row align-items-center ">
                        <div class="col-12 text-center"
                             style="height:80vh;background:url('imagenes/backVanguardiaDarkLight.jpg') no-repeat;background-size: cover">
                            <img style="width: 80%;margin-top: 30%" src="imagenes/logo.png" alt="logo"/>
                        </div>
                    </div>
                </div>

                <div class="col-8 align-self-center " style="background-color: #FFFFFF; height: auto;padding-top: 10%">

                    <div class="row align-items-center">
                        <div class="col-12" style="background-color: #FFFFFF;">
                            <div class="row justify-content-center">
                                <div class="col-12 align-self-center">
                                    <h4 class="modal-title" style="text-align: center">Iniciar Sesión</h4>
                                </div>
                            </div>
                        </div>
                    </div>
                    <form id="login" method="post" action="" style="background-color: #FFFFFF;">
                        <fieldset id="inputs">
                            <div class="row" style="background-color: #FFFFFF;">
                                <div class="col-12">
                                    <input name="user" id="user" height="" type="text"
                                           class="form-control "
                                           placeholder="Usuario" autofocus
                                           data-describedby="mensaje" required>
                                    <span id="mensaje"></span>

                                </div>
                            </div>
                            <div class="row" style="background-color: #FFFFFF;">
                                <div class="col-12">
                                    <input name="pass" id="pass" class="form-control" type="password"
                                           placeholder="Contraseña"
                                           required>
                                    <span id="mensaje_login" style="color:#F7060A;width: 100%"></span>

                                </div>
                            </div>
                        </fieldset>
                        <fieldset class="actions" style="margin: 0">
                            <div class="row" style="background-color: #FFFFFF;">
                                <div class="col-12">

                                    <input style="margin-bottom: 5%" type="submit"
                                           id="Entrar" name="Entrar"
                                           class="submit btn btn-primary form-control col-6 col-sm-8 col-md-12"
                                           value="Entrar">
                                    <a href="#" onClick="dialog_pass();">¿Olvidaste tu Contraseña?</a>


                                </div>
                            </div>
                        </fieldset>

                    </form>
                </div>
            </div>
        </div>
    </div>
</div>


<div id="dlg_pass" style="display:none;">
    <form id="fm_pass" method="post" novalidate>

        <table>

            <tr>
                <td colspan="2">
                    <span>Por favor escribe tu nombre de usuario.</span>
                </td>
            </tr>
            <tr>

                <div class="fitem">

                    <td><label>Usuario:</label></td>

                    <td><input name="usuario_us" id="usuario_us" class="easyui-validatebox" style=" width:180px"
                               data-options="required:true"></td>

                </div>
            </tr>
        </table>
    </form>

</div>
<script src="complemento/jquery-2.2.0.min.js"></script>
<script src="script/jquery.validate.min.js"></script>
<script type="text/javascript" src="./complemento/jquery.easyui.min.js"></script>
<script src="script/login.js"></script>

<script language="javascript" type="application/javascript">
    function loginVan() {

        let user = $('#user').val();
        let pass = $('#pass').val();


        $.ajax({
            type: 'POST',
            url: './php/login.php',
            data: {usuario: user, pass: pass},
            success: function (data) {

                if (data === '1') {
                    document.location = 'views/admin.php';
                } else {
                    $('#mensaje_login').html(data);

                    setTimeout(function () {
                        $('#mensaje_login').html('');
                    }, 3000)
                }

            },
            error: function (jqXHR, textStatus, errorThrown) {

                //console.log(textStatus);

            }
        });

    }

    $('#login').validate({

        rules: {
            user: {
                required: true,
                minlength: 3,
                maxlength: 20
            },
            pass: {
                required: true,
                minlength: 3,
                maxlength: 25
            }
        },
        messages: {

            user: {
                required: " No Escribiste usuario",
                minlength: " Usuario muy corto",
                maxlength: " Usuario muy largo"
            },
            pass: {
                required: " No Escribiste Contraseña",
                minlength: " Contraseña muy corta",
                maxlength: " Contraseña muy larga"
            }

        },
        debug: false,
        errorPlacement: function (error, element) {

            if (element.attr("name") === "user" || element.attr("name") === "pass") {

                $('#mensaje_login').html(error);

                element.css({
                    'border-color': 'red'
                });

            }


        },
        submitHandler: function (form) {

            loginVan();
        }

    });


    function dialog_pass() {

        document.getElementById('dlg_pass').style.display = '';
        $('#dlg_pass').dialog({

            title: 'Recuperacion de Contraseña',
            width: 450,
            height: 150,
            buttons: [{

                text: 'Ok',
                iconCls: 'icon-ok',
                handler: function () {
                    RecoverPass();
                }
            }, {
                text: 'Cancel',
                handler: function () {
                    $('#dlg_pass').dialog('close');
                }
            }
            ]
        });
    }
</script>
</body>
<style>
    h4 {
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    }
</style>
</html>