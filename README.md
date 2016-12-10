# meta-compulab can be used as a separate layer that provides:
MACHINE: cm-fx6-evk (kernel,u-boot), cl-som-imx6ul (kernel,u-boot)

First install the i.MX NXP BSP repo
$: repo init -u git://git.freescale.com/imx/fsl-arm-yocto-bsp.git -b imx-4.1-krogoth

Download the Yocto Project NXP Layers:
$: repo sync

Goto to source directory and clone this meta layer:
$: git clone https://github.com/compulab-yokneam/meta-compulab

NOTE: Add the meta-compulab location to the conf/bblayers.conf manually.
