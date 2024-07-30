<?

/**
 * Created by Dreamweaver.
 * User: Christian
 * Date: 25/01/16
 * Time: 11:20
 */
ob_start();
session_start();


include_once('../../connexion/DataBase.php');
$manager = DataBase::getInstance();

$idPromotor = $_GET['idPromotor'];
$fecha = date('Y-m-d', strtotime($_GET['Fecha']));
//$Fecha=date('d-m-Y',strtotime($fecha_el . " + 1 day"));

//*******************Query de busqueda de Tiendas Visitadas


$sql_prom = "SELECT * FROM Promotores WHERE idCelular=$idPromotor ";
$resul_prom = $manager->ejecutarConsulta($sql_prom);
$datos_prom = mysqli_fetch_array($resul_prom);

?>


<div style="padding: 5px;box-sizing: border-box;width: 100%;height: 100%;">

    <h3 align="center"><?= "Promotor: " . $datos_prom['nombre']; ?></h3>
    <h4 align="center"><?= "Fecha: " . $fecha; ?></h4>

    <table id="rastreo_det"></table>

</div>



<script>


    let id = <?= $idPromotor ?>;
    let fecha= '<?= $fecha ?>';


    $('#rastreo_det').datagrid({

        url:'../php/rastreo/get_detalle_rastreo.php',
        method: 'GET',
        queryParams : {id:id, fecha: fecha},
        columns: [[
            {field:'idTienda', title:'ID Tienda'},
            {field:'sucursal', title:'Sucursal'},
            {field:'distancia', title:'Distancia', formatter: fotmattDistancia},
            {field:'hora', title:'Hora Celular'},
            {field:'tiempollegadaregistro', title:'Hora servidor'},
            {field:'tipo', title:'Tipo Registro'},
            {field:'grupo', title:'Grupo'},
            {field:'visitas', title:'visitas'},
            {field:'autotime', title:'Tiempo automático', formatter: autoTime }

        ]],
        fit:true


    });



    function fotmattDistancia(val, row){

        let distancia = distanciaEntreDosPuntos(row.x, row.y, row.latitud, row.longitud);

        return  distancia >= 1 ? distancia.toFixed(2) + 'km' : (distancia * 1000.0).toFixed(2) + 'm';
    }


    function autoTime(val, row){
        if(row.auto_time == 0 ){
            return `<span style="padding: 5px;box-sizing: border-box;background-color: #ff7272;border-radius: 5px;color: #ffeeee" >Apagado</span>`;
        }
        return '';
    }

</script>




