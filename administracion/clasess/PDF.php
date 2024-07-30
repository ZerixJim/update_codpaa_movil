<?php
/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 26/08/14
 * Time: 18:02
 */
require('fpdf.php');

class PDF extends FPDF{
    private $marca;
    public  $imagen;
    public  $title, $tipo;
    public $angle = 0;

    /**
     * @param mixed $tipo
     */
    public function setTipo($tipo)
    {
        $this->tipo = $tipo;
    }

    /**
     * @return mixed
     */
    public function getTipo()
    {
        return $this->tipo;
    }

    function Header(){
        //logotipo
        $this->Image('../imagenes/fondo.jpg',0,0,$this->w,$this->h);
        $this->Image('../imagenes/LogoSVSsinfondo(opacity58).png',10,15,33);
        $this->Image('../imagenes/marca.png',140,70,160,60);
        $this->SetFont('Arial','B',15);
        $this->Cell(0,20,$this->getTitle(),0,1,'C');



    }

    public function Body($nombrePromotor,$fecha){
        $this->SetFont('Arial','B',15);

        $this->Ln(60);
        $this->Cell(0,10,'Promotor: '.$nombrePromotor,0,0,'R');
        $this->Ln(10);
        $this->Cell(0,10,'Fecha: '.$fecha,0,2,'R');


    }




    /**
     * @param mixed $title
     */
    public function setTitle($title)
    {
        $this->title = $title;
    }

    /**
     * @return mixed
     */
    public function getTitle()
    {
        return $this->title;
    }

    /**
     * @param mixed $imagen
     */
    public function setImagen($imagen)
    {
        $this->imagen = $imagen;
    }

    /**
     * @return mixed
     */
    public function getImagen()
    {
        return $this->imagen;
    }


    function Footer(){
        // Posición: a 1,5 cm del final
        $this->SetY(-15);
        // Arial italic 8
        $this->SetFont('Arial','I',8);
        // Número de página
        $this->Cell(0,10,'Page '.$this->PageNo().'/{nb}',0,1,'C');
        $this->Cell(0,-20,'Vanguardia',0,1,'C');
    }

    function setMarca($marca){
        $this->marca = $marca;

    }

    function getMarca(){
        return $this->marca;
    }

    function Rotate($angle, $x=-1, $y=-1)
    {
        if($x==-1)
            $x=$this->x;
        if($y==-1)
            $y=$this->y;
        if($this->angle!=0)
            $this->_out('Q');
        $this->angle=$angle;
        if($angle!=0)
        {
            $angle*=M_PI/180;
            $c=cos($angle);
            $s=sin($angle);
            $cx=$x*$this->k;
            $cy=($this->h-$y)*$this->k;
            $this->_out(sprintf('q %.5f %.5f %.5f %.5f %.2f %.2f cm 1 0 0 1 %.2f %.2f cm', $c, $s, -$s, $c, $cx, $cy, -$cx, -$cy));
        }
    }

    function _endpage()
    {
        if($this->angle!=0)
        {
            $this->angle=0;
            $this->_out('Q');
        }
        parent::_endpage();
    }

    function RotatedImage($file, $x, $y, $w, $h, $angle)
    {
        //Image rotated around its upper-left corner
        $this->Rotate($angle, $x, $y);
        $this->Image($file, $x, $y, $w, $h);
        $this->Rotate(0);
    }


}


