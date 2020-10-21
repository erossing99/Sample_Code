// Template to complete the patchsym program which can retrive global
// symbol values or change them. Sections that start with a CAPITAL in
// their comments require additions and modifications to complete the
// program.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/mman.h>
#include <elf.h>

int DEBUG = 0;                  // controls whether to print debug messages

#define GET_MODE 1              // only get the value of a symbol
#define SET_MODE 2              // change the value of a symbol

int main(int argc, char **argv){
  // PROVIDED: command line handling of debug option
  if( argc > 1 && strcmp(argv[1], "-d")==0 ){
    DEBUG = 1;                  // check 1st arg for -d debug
    argv++;                     // shift args forward if found
    argc--;
  }

  if(argc < 4){
    printf("usage: %s [-d] <file> <symbol> <type> [newval]\n",argv[0]);
    return 0;
  }

  int mode = GET_MODE;          // default to GET_MODE
  char *new_val = NULL;
  if(argc == 5){                // if an additional arg is provided run in SET_MODE
    printf("SET mode\n");
    mode = SET_MODE;
    new_val = argv[4];
  }
  else{
    printf("GET mode\n");
  }
  char *objfile_name = argv[1];
  char *symbol_name = argv[2];
  char *symbol_kind = argv[3];

  // PROVIDED: open file to get file descriptor
  int fd = open(objfile_name, O_RDWR);

  // DETERMINE size of file and create read/write memory map of the file
  struct stat stat_buf;
  fstat(fd, &stat_buf);
  int size = stat_buf.st_size;
  char *file_bytes = mmap(NULL, size, PROT_READ|PROT_WRITE, MAP_SHARED, fd, 0);
  // CREATE A POINTER to the intial bytes of the file which are an ELF64_Ehdr struct
  Elf64_Ehdr *ehdr = (Elf64_Ehdr *) file_bytes;

  // CHECK e_ident field's bytes 0 to for for the sequence {0x7f,'E','L','F'}.
  // Exit the program with code 1 if the bytes do not match
  int ident_matches =
    ehdr->e_ident[0] == 0x7f &&
    ehdr->e_ident[1] == 'E' &&
    ehdr->e_ident[2] == 'L' &&
    ehdr->e_ident[3] == 'F';

  if (!ident_matches){
    printf("ERROR: Magic bytes wrong, this is not an ELF file\n");
    return 1;
  }

  // PROVIDED: check for a 64-bit file
  if(ehdr->e_ident[EI_CLASS] != ELFCLASS64){
    printf("ERROR: Not a 64-bit file ELF file\n");
    // UNMAP, CLOSE FD
    return 1;
  }

  // PROVIDED: check for x86-64 architecture
  if(ehdr->e_machine != EM_X86_64){
    printf("ERROR: Not an x86-64 file\n");
    // UNMAP, CLOSE FD
    return 1;
  }

  // DETERMINE THE OFFSET of the Section Header Array (e_shoff), the
  // number of sections (e_shnum), and the index of the Section Header
  // String table (e_shstrndx). These fields are from the ELF File
  // Header.
  int secHeadArrayOffset = ehdr->e_shoff;
  int numSections = ehdr->e_shnum;
  int secHeadStrTableIndex = ehdr->e_shstrndx;

  // SET UP a pointer to the array of section headers. Use the section
  // header string table index to find its byte position in the file
  // and set up a pointer to it.
  Elf64_Shdr *sec_hdrs = (Elf64_Shdr *) (file_bytes + secHeadArrayOffset);
  size_t sec_names_offset = sec_hdrs[secHeadStrTableIndex].sh_offset;
  char *secStringTable = (char *) (file_bytes + sec_names_offset);

  // potentially introduce new variables.
  long symtabOffset = -1; //assigning to -1 for error checking
  long symtabSize;
  long symtabEntries;
  int symtabIndex;
  long strtabOffset = -1;
  //int strtabIndex;
  long dataOffset = -1;
  int dataIndex;
  long dataAddress = -1;
  // SEARCH the Section Header Array for sections with names .symtab
  // (symbol table) .strtab (string table), and .data (initialized
  // data sections).  Note their positions in the file (sh_offset
  // field).  Also note the size in bytes (sh_size) and and the size
  // of each entry (sh_entsize) for .symtab so its number of entries
  // can be computed. Finally, note the .data section's index (i value
  // in loop) and its load address (sh_addr).
  Elf64_Shdr currentSection;
  size_t nameOffset;
  char *currString;
  for(int i=0; i<numSections; i++){
    currentSection = sec_hdrs[i];
    nameOffset = currentSection.sh_name;
    currString = (char *) (secStringTable + nameOffset);
    if (strcmp(currString,".symtab") == 0){
      symtabOffset = currentSection.sh_offset;
      symtabSize = currentSection.sh_size;
      symtabEntries = currentSection.sh_entsize;
      symtabIndex = i;
    }
    else if (strcmp(currString,".strtab") == 0){
      strtabOffset = currentSection.sh_offset;
      //strtabIndex = i;
    }
    else if (strcmp(currString,".data") == 0){
      dataOffset = currentSection.sh_offset;
      dataIndex = i;
      dataAddress = currentSection.sh_addr;
    }
  }


  // ERROR check to ensure everything was found based on things that
  // could not be found.
  if(symtabOffset == -1){
    printf("ERROR: Couldn't find symbol table\n");
    munmap(file_bytes, size);
    close(fd);
    return 1;
  }

  if(strtabOffset == -1){
    printf("ERROR: Couldn't find string table\n");
    munmap(file_bytes, size);
    close(fd);
    return 1;
  }

  if(dataOffset == -1){
    printf("ERROR: Couldn't find data section\n");
    munmap(file_bytes, size);
    close(fd);
    return 1;
  }

  // PRINT info on the .data section where symbol values are stored

  printf(".data section\n");
  printf("- %hd section index\n",dataIndex);
  printf("- %lu bytes offset from start of file\n",dataOffset);
  printf("- 0x%lx preferred virtual address for .data\n",dataAddress);


  // PRINT byte information about where the symbol table was found and
  // its sizes. The number of entries in the symbol table can be
  // determined by dividing its total size in bytes by the size of
  // each entry.

  printf(".symtab section\n");
  printf("- %hd section index\n",symtabIndex);
  printf("- %lu bytes offset from start of file\n",symtabOffset);
  printf("- %lu bytes total size\n",symtabSize);
  printf("- %lu bytes per entry\n",symtabEntries);
  printf("- %lu entries\n",(symtabSize / symtabEntries));

  // SET UP pointers to the Symbol Table and associated String Table
  // using offsets found earlier. Then SEARCH the symbol table for for
  // the specified symbol.
  Elf64_Sym *symtab = (Elf64_Sym *) (file_bytes + symtabOffset);
  char *strtab = (char *) (file_bytes + strtabOffset);
  Elf64_Sym currentSymbolSect;
  char *currentString;

  for(int i=0; i < (int)(symtabSize / symtabEntries); i++){
    currentSymbolSect = symtab[i];
    currentString = (char *) (strtab + currentSymbolSect.st_name);
    if(strcmp(currentString, symbol_name) == 0 ){
      // If symbol at position i matches the 'symbol_name' variable
      // defined at the start of main(), it is the symbol to work on.
      // PRINT data about the found symbol.

      printf("Found Symbol '%s'\n",symbol_name);
      printf("- %d symbol index\n",i);
      printf("- 0x%lx value\n",currentSymbolSect.st_value);
      printf("- %lu size\n",currentSymbolSect.st_size);
      printf("- %hu section index\n",currentSymbolSect.st_shndx);

      // CHECK that the symbol table field st_shndx matches the index
      // of the .data section; otherwise the symbol is not a global
      // variable and we should bail out now.
      if( currentSymbolSect.st_shndx != dataIndex ){
        printf("ERROR: '%s' in section %hd, not in .data section %hd\n",symbol_name,currentSymbolSect.st_shndx,dataIndex);
        munmap(file_bytes, size);
        close(fd);
        return 1;
      }

      // CALCULATE the offset of the value into the .data section. The
      // 'value' field of the symbol is its preferred virtual
      // address. The .data section also has a preferred load virtual
      // address. The difference between these two is the offset into
      // the .data section of the mmap()'d file.
      size_t valueOffset = (size_t) (currentSymbolSect.st_value - dataAddress);

      printf("- %ld offset in .data of value for symbol\n",valueOffset);

      // Symbol found, location in .data found, handle each kind (type
      // in C) of symbol value separately as there are different
      // conventions to change a string, an int, and so on.

      // string is the only required kind to handle
      if( strcmp(symbol_kind,"string")==0 ){
        // PRINT the current string value of the symbol in the .data section
        char *currVal = (char *) (file_bytes + dataOffset + valueOffset);
        printf("string value: '%s'\n",currVal);

        // Check if in SET_MODE in which case change the current value to a new one
        if(mode == SET_MODE){
          long currLength = currentSymbolSect.st_size;
          long newLength = strlen(new_val);
          // CHECK that the length of the new value of the string in
          // variable 'new_val' is short enough to fit in the size of
          // the symbol.
          if( newLength > currLength ){
            // New string value is too long, print an error
            printf("ERROR: Cannot change symbol '%s': existing size too small\n",symbol_name);
            printf("Cur Size: %lu '%s'\n", currLength, currVal);
            printf("New Size: %lu '%s'\n", newLength, new_val);
            munmap(file_bytes, size);
            close(fd);
            return 1;
          }
          else{
            // COPY new string into symbols space in .data as it is big enough
            for(int i=0; i < currLength; i++){
              if(i < newLength){
                currVal[i] = new_val[i];
              }
              else{
                currVal[i] = '\0';
              }
            }
            // PRINT the new string value for the symbol
            printf("New val is: '%s'\n", currVal);
          }
        }
      }

      // OPTIONAL: fill in else/ifs here for other types on might want
      // to support such as int and double

      else{
        printf("ERROR: Unsupported data kind '%s'\n",symbol_kind);
        munmap(file_bytes, size);
        close(fd);
        return 1;
      }

      // succesful completion of getting or setting the symbol
      munmap(file_bytes, size);
      close(fd);
      return 0;
    }
  }

  // Iterated through whole symbol tabel and did not find symbol, error out.
  printf("ERROR: Symbol '%s' not found\n",symbol_name);
  munmap(file_bytes, size);
  close(fd);
  return 1;
}
