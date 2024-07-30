<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    ?>
    <html>
    <head>

    </head>

    <body>
    <?

    include_once('../../php/seguridad.php');

    ?>
    <div align="center">
        <form id="fm_sendSM" method="post" enctype="multipart/form-data">

            <table width="100%" border="0" cellspacing=0 cellpadding=2>
                <tr>

                    <div class="fitem">

                        <td align="right" height="35"><label><strong>Guia:</strong></label></td>

                        <td align="center">
                            <input name="num_guiaSM" id="num_guiaSM" class="easyui-textbox" style="width:200px"
                                   required></td>

                    </div>
                </tr>

            </table>

        </form>
    </div>

    </body>
    </html>

    <?
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>