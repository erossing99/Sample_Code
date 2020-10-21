    .text

    .globl set_batt_from_ports

set_batt_from_ports:
    movl  $0,%eax            #prep %eax for returning 0 as long as there are no errors

    movw BATT_VOLTAGE_PORT(%rip), %cx    #retrieve value of BATT_VOLTAGE_PORT and put in register %ecx
    cmpw  $0,%cx
    jl    .VOLTAGENEGATIVE            #if BATT_VOLTAGE_PORT is less than zero jump to error marker
    jmp   .CONTINUE1         #if BATT_VOLTAGE_PORT is not less than 0 we will continue

.CONTINUE1:       #start retrieving data from ports
    movw	%cx, 0(%rdi)       #move BATT_VOLTAGE_PORT value to *batt voltage field
    movw	BATT_VOLTAGE_PORT(%rip), %dx    #retrieve value of BATT_VOLTAGE_PORT and put in register %edx
    subl	$3000, %edx        #subtract 3000 from %edx
    sarl  $3,%edx            #divide %edx by 8 using right shift of 3 bits
    movb  %dl, 2(%rdi)       #move percent value into percent field of *batt
    cmpb  $100,%dl
    jle   .CONTINUE2         #if percent is not greater than 100 we'll continue
    movb  $100, 2(%rdi)      #if percent is greater than 100 then well reset its value to 100

.CONTINUE2:
    cmpw  $3800,%cx          #compare 3800 to voltage value
    jle   .CONTINUE3         #if voltage is above 3800 then set percent to 100
    movb  $100, 2(%rdi)

.CONTINUE3:
    cmpw  $3000,%cx         #compare 3000 to voltage value
    jge   .CONTINUE4        #if voltage is less than 3000 then set percent to 0
    movb  $0, 2(%rdi)

.CONTINUE4:
    testb	$1, BATT_STATUS_PORT(%rip)    #tests if rightmost digit of BATT_STATUS_PORT is 1
    jne   .IS1              #if it is 1 then it will jump to is 1
    movb  $0, 3(%rdi)       #if the rightrmost digit is zero it will set batt->mode to zero
    movl  $0,%eax
    ret

.IS1:
    movb  $1, 3(%rdi)       #sets batt->mode to 1 then returns
    movl  $0,%eax
    ret

.VOLTAGENEGATIVE:          #if error occurs with negative voltage input it will jump here
    movl  $1,%eax          #if BATT_VOLTAGE_PORT is less than zero set %eax to 1 and return
    ret

#end of set_batt_from_ports function

    .section  .data
    array:                      #array of bitmasks
          .int  0b0111111     #array[0] = 0b0111111
          .int  0b0000011     #array[1] = 0b0000011 etc.
          .int  0b1101101
          .int  0b1100111     #access index i of array with:
          .int  0b1010011     #leaq  array(%rip),%r8  #r8 points to array
          .int  0b1110110     #movq  $2,%r9    #r9 = 2
          .int  0b1111110     #movl  (%r8,%r9,4),%r10  #r10 = array[2]
          .int  0b0100011
          .int  0b1111111
          .int  0b1110111

    .globl set_display_from_batt

set_display_from_batt:
    movl  %edi,%r8d      #move batt in %r8d
    sarl  $24,%r8d       #shift right to get rid of lower order zeros
    cmpb  $0, %r8b       #checking what mode batt is in
    jne   .PERCENTMODE   #if batt.mode not equal to 0 go to percent mode
    jmp   .VOLTAGEMODE   #if batt.mode equals 0 go to voltage mode

.VOLTAGEMODE:
    movl  %edi,%r8d      #reset %r8 to full packed batt struct
    andl  $0xFFFF,%r8d   #batt.volts is now in %r8d
    cmpw  $1000,%r8w     #check if voltage is greater than 1000
    jle   .CONTINUE5     #if voltage is not over 1000 we will continue
    movl  %r8d,%eax      #move battvolts into division register %rax
    cqto                 #extend unsigned
    movl  $10,%r11d
    idivl %r11d          #divide batt.volts by 10
    #remainder is now in %rdx
    movl  %eax,%r8d      #move volts/10 into %r8d
    cmpl  $5,%edx        #compare 5 and remainder
    jl    .SKIPROUNDING
    addl  $1,%r8d        #if remainder is >= 5 then add 1 to volts

.SKIPROUNDING:           #may be unnecessary but I did it for readability
    jmp   .CONTINUE5

.CONTINUE5:
    movl  $0,%ecx             #creating new bit string for the display
    movl  %r8d,%eax           #move volts value into %rax for division
    cqto
    movl  $10,%r11d
    idivl %r11d               #divide volts by 10, puts volts/10 in %rax and volts%10 in %rdx
    leaq  array(%rip),%r8     #%r8 now points to my bitmask array
    movl  (%r8,%rdx,4),%r9d   #shifts to correct position in the bitmask array and puts that bit in %r9d
    andl  $0xFF,%r9d          #zeros out higher order bits
    orl   %r9d,%ecx           #or current display val with rightmost number bitmask
    jmp   .MIDDLEDIGIT

.MIDDLEDIGIT:
    cqto
    movl  $10,%r11d
    idivl %r11d               #divide %rax by 10 again to get to middle digit
    movl  (%r8,%rdx,4),%r9d   #shifts to correct bitmask in %r10 and puts this val in %r9d
    sall  $7,%r9d             #left shift bit value to correct position
    andl  $0xFFFF,%r9d        #zeros out higher order bits
    orl   %r9d,%ecx           #or display val with middle number bitmask
    jmp   .LEFTDIGIT

.LEFTDIGIT:
    cqto
    movl  $10,%r11d
    idivl %r11d               #divide %rax by 10 again to get to leftmost digit
    movl  (%r8,%rdx,4),%r9d   #shifts to correct bitsmask in %r10, puts this val in %r9d
    sall  $14,%r9d            #left shift %r9d to correct position for display val
    andl  $0xFFFFFF,%r9d      #zeros out higher order bits
    orl   %r9d,%ecx           #add leftmost value bitmask to current display val
    jmp   .CONTINUE6

    #digits have been added to current display value

.CONTINUE6:
    movl  $0b1,%r9d           #put bit value 0b1 in %r9d
    sall  $22,%r9d            #left shift %r9d by 22
    orl   %r9d,%ecx           #or current display value with %r9d to turn on V indicator
    sarl  $1,%r9d             #right shift %r9d by 1 to get it to position 21
    orl   %r9d,%ecx           #or current display value with %r9d to turn on decimal point
    #beginning to set the battery level indicator
    movq  %rdi,%r8            #packed batt struct back in %r8
    sarl  $16,%r8d            #shift right to delete volts field
    andq  $0xFF,%r8           #and out all higher order 1s with &0xFF
    cmpl  $0, %r8d            #percent field now in %r8
    jne   .NOTEQUAL           #if percent not equal to zero go here
    jmp   .ENDFORVOLTAGE      #if percent is equal to zero were skipping to return

.NOTEQUAL:
    cmpl  $5, %r8d
    jge   .GREATEREQ          #if percent is >=5 then first condition matches so we check next
    jmp   .MATCHFAILED1       #if not, skip to next else if condition

.GREATEREQ:
    cmpl  $29, %r8d
    jg   .MATCHFAILED1        #if percent >29 then skip to next conditional check
    movl  $0b1,%r9d           #else put binary val 0b1 in %r9d
    sall  $28,%r9d            #shift %r9d left 28 bits
    orl   %r9d,%ecx           #or %r9d with current display value in order to turn on first level of battery indicator
    jmp   .ENDFORVOLTAGE      #jump to end of function

.MATCHFAILED1:
    cmpl  $49,%r8d            #same operation as in GREATEREQ just shifted differently and with different bitmask
    jg    .MATCHFAILED2
    movl  $0b11,%r9d
    sall  $27,%r9d
    orl   %r9d,%ecx
    jmp   .ENDFORVOLTAGE

.MATCHFAILED2:                #same operation as in GREATEREQ just shifted differently and with different bitmask
    cmpl  $69, %r8d
    jg    .MATCHFAILED3
    movl  $0b111,%r9d
    sall  $26,%r9d
    orl   %r9d,%ecx
    jmp   .ENDFORVOLTAGE

.MATCHFAILED3:                #same operation as in GREATEREQ just shifted differently and with different bitmask
    cmpl  $89, %r8d
    jg    .MATCHFAILED4
    movl  $0b1111,%r9d
    sall  $25,%r9d
    orl   %r9d,%ecx
    jmp   .ENDFORVOLTAGE

.MATCHFAILED4:                #same operation as in GREATEREQ just shifted differently and with different bitmask
    cmpl  $100, %r8d
    jg    .MATCHFAILED5
    movl  $0b11111,%r9d
    sall  $24,%r9d
    orl   %r9d,%ecx
    jmp   .ENDFORVOLTAGE

.MATCHFAILED5:
    jmp   .ENDFORVOLTAGE

.ENDFORVOLTAGE:
    movl  %ecx,(%rsi)         #move new display value into second argument *display
    movl  $0, %eax            #prepping return value
    ret

.PERCENTMODE:                 #starts process for altering display in percent mode
    movl  %edi,%r8d           #move packed batt struct into %r8
    sarl  $16,%r8d            #delete volts field from batt struct
    andl  $0xFF,%r8d          #zeroing out higher order bits than the first 8 for char percent
    movl  $0,%ecx             #creating new bit string for the display
    movl  %r8d,%eax           #move percent value into %rax for division
    cqto                      #extend sign
    movl  $10,%r11d
    idivl %r11d               #divide percent by 10, puts percent/10 in %eax and percent%10 in %edx
    leaq  array(%rip),%r10    #%r10 now points to my bitmask array
    movl  (%r10,%rdx,4),%r9d  #shits to correct position in the bitmask array and puts
                              #that byte in %r9d, b/c being put into 32 bit register it should
                              #zero out all higher 32 bits
    orl   %r9d,%ecx           #or current display val with rightmost number bitmask
    jmp   .MIDDLEDIGITPERCENT #finished alter right digit move on to middle digit

.MIDDLEDIGITPERCENT:
    cqto
    movl  $10,%r11d
    idivl %r11d               #divide %rax by 10 again to get to middle digit in %edx
    movl  %edx,%r8d           #store middle digit in %r8d
    cqto
    movl  $10,%r11d
    idivl %r11d               #divide %rax by 10 again to get leftmost digit in %edx
    cmpl  $0,%edx
    jne   .LEFTNOTEQUALZERO   #checking if left is not equal to zero first because then I can perform bit ops for left and middle digit
    cmpl  $0,%r8d
    jne   .JUSTMIDDLENOTEQUALZERO   #jumps here if only the middle digit not equal to zero
    jmp   .CONTINUE7          #jumps only if both middle and left digit are zero

.LEFTNOTEQUALZERO:
    movl  (%r10,%r8,4),%r9d   #retrieve bitmask for middle digit and put it in %r9d
    sall  $7,%r9d             #shift bit mask to correct position
    orl   %r9d,%ecx           #or current display val with middle digit bit mask
    jmp   .LEFTDIGITPERCENT   #jump to point where we set the left digit bit mask

.LEFTDIGITPERCENT:
    movl  (%r10,%rdx,4),%r9d
    sall  $14,%r9d
    orl   %r9d,%ecx
    jmp   .CONTINUE7

.JUSTMIDDLENOTEQUALZERO:
    movl  (%r10,%r8,4),%r9d   #retrieve bitmask for middle digit and put it in %r9d
    sall  $7,%r9d
    orl   %r9d,%ecx           #or current display val with middle digit bit mask
    jmp   .CONTINUE7

#all percent values have been set in current display value at this point

.CONTINUE7:   #beginning to set batterly level indicator
    movl  $0b1,%r8d           #put binary val 0b1 in %r8d
    sall  $23,%r8d            #shift 0b1 left by 23 spaces
    orl   %r8d,%ecx           #set percent indicator
    movl  %edi,%r8d           #move packed batt struct into %r8
    sarl  $16,%r8d            #delete volts field from batt struct
    andl  $0xFF,%r8d          #zeroing out higher order bits than the first 8 for char percent
    cmpl  $5,%r8d
    jge   .MATCHFAILED6       #if percent is >=5 then move on to next conditional check
    jmp   .ENDFORPERCENT      #if percent is <5 skip to end of code

.MATCHFAILED6:  #if we reach this point we know that percent is >=5 already
    cmpl  $29,%r8d
    jg    .MATCHFAILED7       #if percent is greater than 29 then we will skip to next conditional check
    movl  $0b1,%edx
    sall  $28,%edx            #shift %edx left 28 spaces to correct position in display bit value
    orl   %edx,%ecx           #or current display value with %edx
    jmp   .ENDFORPERCENT

.MATCHFAILED7:
    cmpl  $49,%r8d
    jg    .MATCHFAILED8       #if percent >49 then skip to next check
    movl  $0b11,%edx
    sall  $27,%edx
    orl   %edx,%ecx           #mask portion of current display to turn on battery levels
    jmp   .ENDFORPERCENT

.MATCHFAILED8:
    cmpl  $69,%r8d
    jg    .MATCHFAILED9       #if percent >69 then skip to next check
    movl  $0b111,%edx
    sall  $26,%edx
    orl   %edx,%ecx           #mask portion of current display to turn on battery levels
    jmp   .ENDFORPERCENT

.MATCHFAILED9:
    cmpl  $89,%r8d
    jg    .MATCHFAILED10      #if percent >89 then skip to next check
    movl  $0b1111,%edx
    sall  $25,%edx
    orl   %edx,%ecx           #mask portion of current display to turn on battery levels
    jmp   .ENDFORPERCENT

.MATCHFAILED10:
    cmpl  $100,%r8d
    jg    .ENDFORPERCENT      #if percent >89 then skip to next check
    movl  $0b11111,%edx
    sall  $24,%edx
    orl   %edx,%ecx           #mask portion of current display to turn on battery levels
    jmp   .ENDFORPERCENT

.ENDFORPERCENT:
    movl  %ecx,(%rsi)         #move new display value into second argument *display
    movl  $0, %eax            #prepping return value
    ret

#End of set_display_from_batt function

    .globl batt_update

batt_update:
    #storing batt struct in %r8
    pushq $0                  #allocating space for a zero batt
    leaq  0(%rsp),%rdi        #moving memory address of batt struct into %rdi for first argument
    call  set_batt_from_ports
    cmpl  $1,%eax
    je    .ERROR              #if the return value equals one there was an error so jump to ERROR
    movl  (%rsp),%edi         #place batt struct into %edi
    leaq  0(%rsp),%rsi        #put pointer to top of stack in %rsi as the pointer to the original display value
    call  set_display_from_batt
    popq  %rdx                #realign stack
    movl  %edx, BATT_DISPLAY_PORT(%rip)
    movl  $0,%eax             #put zero in return register
    ret

.ERROR:
    popq  %rdx                #realign stack
    ret
