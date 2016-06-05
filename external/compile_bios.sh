#!/bin/bash

drivers="monitor.h monitor.c peripheral.h peripheral.c floppydrive.h floppydrive.c harddrive.h harddrive.c"
lib="system.h system.c stdio.h stdio.c util.h util.c"
input="bios/main.c"
linker_script="bios/linker.ld"
temp="bios.elf"
output="bios.bin"

for i in $drivers ; do
     input="$input drivers/$i"
done
for i in $lib ; do
     input="$input lib/$i"
done

linker_flags="-Wl,-T,$linker_script"
flags="-static -ffreestanding -G0 -g -O1 -fno-toplevel-reorder -fomit-frame-pointer $linker_flags -nostdlib"

mipsel-none-elf-gcc $flags -o $temp $input && \
mipsel-none-elf-objcopy -Obinary $temp $output && \
echo "Succesfuly compiled"

rm -f $temp
