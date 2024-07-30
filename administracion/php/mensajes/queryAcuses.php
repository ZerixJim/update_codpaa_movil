<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {


    $tipoAcuse = $_REQUEST['tipoAcu'];
    $idMsj = $_REQUEST['idMsj'];
    include_once('../../connexion/bdManager.php');

    $manager = new bdManager();
    ?>


    <div id="det_vist" style="margin-top:15%">
        <?
        //***** Seleccionamos los datos del mensaje
        $sql_msj = "select * from mensajes where id_mensaje=$idMsj";
        $rs_msj = $manager->ejecutarConsulta($sql_msj);
        $dat_msj = mysqli_fetch_array($rs_msj);

        if ($tipoAcuse == 1) {
            $query_acuses = "select p.idCelular as id_promotor,p.nombre,ml.fecha_captura as fecha
			from mensaje_leido ml inner join Promotores p on (p.idCelular=ml.id_promotor) 
			where id_mensaje=$idMsj";


        } else {
            $query_acuses = "select p.idCelular as id_promotor, p.nombre from Promotores p 
				inner join marcaAsignadaPromotor mp on (p.idCelular=mp.idPromotor)
				where mp.idMarca='" . $dat_msj['id_marca'] . "' and p.status='a' and token_gcm!='' 
				and p.idCelular NOT IN (select id_promotor from mensaje_leido where id_mensaje=. $idMsj )";
        }
        $rs_acuses = $manager->ejecutarConsulta($query_acuses);


        ?>
        <table id="acuses_det" width="auto">

            <thead>
            <tr>
                <th data-options="field:'id_promotor'">IdProm</th>
                <th data-options="field:'nombre'">Promotor</th>
                <? if ($tipoAcuse == 1) {
                    ?>
                    <th data-options="field:'fecha'">Fecha</th>
                <? } ?>
            </tr>
            </thead>
            <tbody>
            <?
            //************* Recorremos la ruta de la semana elegida
            while ($dat_acuses = mysqli_fetch_array($rs_acuses)) {

                ?>
                <tr>
                    <td><? echo $dat_acuses['id_promotor']; ?></td>
                    <td><? echo $dat_acuses['nombre']; ?></td>
                    <? if ($tipoAcuse == 1) {
                        ?>
                        <td><? echo $dat_acuses['fecha']; ?></td>
                    <? } ?>
                </tr>

                <?
            }
            ?>
            </tbody>

        </table>
    </div>
    <script type="application/javascript">
        $('#acuses_det').datagrid();
    </script>


    <?


} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>