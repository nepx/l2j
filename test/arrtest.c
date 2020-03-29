#include <stdio.h>

const char msg[] = "Hello, world!\n";

int main(){
	puts(msg);
	puts(&msg[1]);
	puts(&msg[2]);
	puts(&msg[3]);
	puts(&msg[4]);
	puts(&msg[5]);
	puts(&msg[6]);
	return 0;
}
