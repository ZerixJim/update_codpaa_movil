<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) :
    ?>

    <?
    $id_promo = $_REQUEST['idPromotor'];

    include_once('../../connexion/DataBase.php');

    $manager = DataBase::getInstance();

    $query_prom = "Select * from Promotores p where idCelular='" . $id_promo . "'";

    $resul_prom = $manager->ejecutarConsulta($query_prom);

    $datos_prom = mysqli_fetch_array($resul_prom);


    ?>
    <div>
        <form id="fm_view" method="post" novalidate class="form-style">


            <ul>
                <li>
                    <label for="nombreE"><strong>Nombre</strong></label>
                    <input name="nombreE" id="nombreE" class="easyui-textbox"
                           data-options="value:'<?= $datos_prom['nombre']; ?>'" <? if ($_SESSION['id_perfil'] == '2') {
                        echo "readonly";
                    } ?>>
                </li>
                <li>
                    <label for="fechaBaja"><strong>Fecha Baja:</strong></label>
                    <input id="fechaBaja" class="easyui-datebox"
                           data-options="formatter:myformatter,parser:myparser,required:true"
                           placeholder="ej.02-03-2014">
                </li>

            </ul>

        </form>
    </div>


<?
else :
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
endif;

?>