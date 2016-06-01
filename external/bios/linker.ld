
OUTPUT_FORMAT("elf32-littlemips");
OUTPUT_ARCH(mips);

SECTIONS
{
    . = 0x0400;
    .text : { *(.text) }
    .rodata : { *(.rodata*) }
    .data : { *(.data) }
    .bss : { *(.bss) }
}

