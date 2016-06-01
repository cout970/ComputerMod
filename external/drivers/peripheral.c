//
// Created by cout970 on 21/05/2016.
//

#include "peripheral.h"

void* getPeripheral(int bus_addr){
    return (void*)(0xFF000000 | (bus_addr << 16));
}