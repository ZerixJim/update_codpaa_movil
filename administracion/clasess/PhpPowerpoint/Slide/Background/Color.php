<?php
namespace PhpOffice\PhpPowerpoint\Slide\Background;
use PhpOffice\PhpPowerpoint\Slide\AbstractBackground;
use PhpOffice\PhpPowerpoint\Style\Color as StyleColor;
class Color extends AbstractBackground
{
    /**
     * @var StyleColor
     */
    protected $color;
    /**
     * @param StyleColor|null $color
     * @return $this
     */
    public function setColor(StyleColor $color = null)
    {
        $this->color = $color;
        return $this;
    }
    /**
     * @return StyleColor
     */
    public function getColor()
    {
        return $this->color;
    }
}
?>