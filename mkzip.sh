zip save rapport.pdf 
dir=$(basename $(pwd))
cd .. && find $dir -name "*.java" -print | zip -r $dir/save -@
