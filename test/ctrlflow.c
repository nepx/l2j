int main(int argc, char** argv){
	if(argc < 2) {
		return 0;
	}else{
		int x = 0;
		for(int i=0;i<argc;i++){
			x += i - argc;
		}
		while(x >= 0){
			x -= argc;
		}
		do {
			x += argc + 5;
		while(x < 100);
		
		return x;
	}
}
