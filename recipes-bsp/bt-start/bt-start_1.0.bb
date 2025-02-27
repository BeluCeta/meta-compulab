SUMMARY = "Murata Bluetooth Start Script"
DESCRIPTION = "initscript to set up the Murata Bluetooth."
SECTION = "base"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
PACKAGE_ARCH = "${MACHINE_ARCH}"
PR = "r1"

INHIBIT_DEFAULT_DEPS = "1"

# As the recipe doesn't inherit systemd.bbclass, we need to set this variable
# manually to avoid unnecessary postinst/preinst generated.
python __anonymous() {
    if not bb.utils.contains('DISTRO_FEATURES', 'sysvinit', True, False, d):
        d.setVar("INHIBIT_UPDATERCD_BBCLASS", "1")
}

inherit update-rc.d systemd

SRC_URI = "file://bt-start \
	   file://bt-start.service \
	   file://GPLv2.patch"

SERVICE_NAME = "bt-start.service"
INITSCRIPT_NAME = "bt-start"
INITSCRIPT_PARAMS = "start 2 3 4 5"

S = "${WORKDIR}"

ALLOW_EMPTY:${PN} = "1"
RDEPENDS:${PN} = "bash"
FILES:${PN} += "${systemd_unitdir}/* ${sysconfdir}/* ${sbindir}/*"

do_install () {

    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/${INITSCRIPT_NAME} ${D}${sbindir}

    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
            install -d ${D}${sysconfdir}/init.d
            ln -s ${sbindir}/${INITSCRIPT_NAME} ${D}${sysconfdir}/init.d/
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -m 644 ${WORKDIR}/${SERVICE_NAME} ${D}/${systemd_unitdir}/system/
    fi

}

pkg_postinst:${PN} () {
	if [ -n "$D" ]; then
		OPTS="--root=$D"
	fi
	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		systemctl $OPTS enable bt-start.service
	fi
	if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
		update-rc.d $OPTS bt-start defaults
	fi
}

pkg_postrm:${PN} () {
	if [ -n "$D" ]; then
		OPTS="--root=$D"
	fi
	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		systemctl $OPTS disable bt-start.service
	fi
	if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
		update-rc.d $OPTS bt-start remove
	fi
}
