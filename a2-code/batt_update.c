#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "batt.h"

int set_batt_from_ports(batt_t *batt){

  if (BATT_VOLTAGE_PORT < 0){
    return 1;
  }

  batt->volts = BATT_VOLTAGE_PORT;
  batt->percent = (BATT_VOLTAGE_PORT - 3000) / 8;
  if (batt->percent > 100){ //checks if over full
    batt->percent = 100;
  }
  if (batt->volts > 3800){ //checks if over full
    batt->percent = 100;
  }
  if (batt->volts < 3000){ //checks if empty
    batt->percent = 0;
  }
  int typemode = 0b1;  // rightmost digit is 1
  if (BATT_STATUS_PORT & typemode){ // checks if BATT_STATUS_PORT's rightmost digit is 1
    batt->mode = 0b1;
  }
  else{ //rightmost digit is zero
    batt->mode = 0b0;
  }

  return 0;
}


int set_display_from_batt(batt_t batt, int *display){

  int digitBitMasks[] = {0b0111111, 0b0000011, 0b1101101, 0b1100111, 0b1010011, 0b1110110, 0b1111110, 0b0100011, 0b1111111, 0b1110111}; //bit masks for digits 0-9
  if (batt.mode == 0){ //in voltage mode
    int volts = batt.volts;
    if (volts > 1000){
      int fourthDigit = volts % 10;
      volts = volts / 10;
      if (fourthDigit >= 5){
        volts++;
      }
    }
    int right = volts % 10;
    volts = volts / 10;  //strips off farthest right number
    int middle = volts % 10;
    volts = volts / 10;
    int left = volts % 10;
    int current = 0;
    current = current | (digitBitMasks[left] << 14);
    current = current | (digitBitMasks[middle] << 7);
    current = current | digitBitMasks[right];
    current = current | (0b1 << 22) ; //setting V indicator
    current = current | (0b1 << 21);  //turning on decimal point
    if (batt.percent == 0){
      current = current;
    }
    else if (batt.percent >= 5 && batt.percent <= 29){ //begins setting the battery level indicator
      current = current | (0b1 << 28);
    }
    else if (batt.percent <= 49){
      current = current | (0b11 << 27);
    }
    else if (batt.percent <= 69){
      current = current | (0b111 << 26);
    }
    else if (batt.percent <= 89){
      current = current | (0b1111 << 25);
    }
    else if (batt.percent <= 100){
      current = current | (0b11111 << 24);
    }
    *display = current;
  } //end of voltage mode

  else{ //in percent mode
    int percent = batt.percent;
    int right = percent % 10;
    percent = percent / 10;
    int middle = percent % 10;
    percent = percent / 10;
    int left = percent % 10;
    int current = 0;
    if (left != 0){
      current = current | (digitBitMasks[left] << 14);
    }
    if (middle != 0 || left != 0){
      current = current | (digitBitMasks[middle] << 7);
    }
    current = current | digitBitMasks[right];
    current = current | (0b1 << 23) ; //setting percent indicator
    if (batt.percent < 5){
      current = current;
    }
    else if (batt.percent >= 5 && batt.percent <= 29){ //begins setting the battery level indicator
      current = current | (0b1 << 28);
    }
    else if (batt.percent <= 49){
      current = current | (0b11 << 27);
    }
    else if (batt.percent <= 69){
      current = current | (0b111 << 26);
    }
    else if (batt.percent <= 89){
      current = current | (0b1111 << 25);
    }
    else if (batt.percent <= 100){
      current = current | (0b11111 << 24);
    }
    *display = current;
  } //end of percent mode
  return 0;
}


int batt_update(){
  batt_t tempBatt = {.volts=-100, .percent=-1, .mode=-1};
  int result = set_batt_from_ports(&tempBatt);
  if (result == 1){
    return 1;
  }
  int newDisplay = 0;
  set_display_from_batt(tempBatt, &newDisplay);
  BATT_DISPLAY_PORT =  newDisplay;
  return 0;
}
