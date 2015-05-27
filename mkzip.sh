dir=$(basename $(pwd))
cd .. && find $dir -name "*.java" -print | zip -r $dir/save -@
zip $dir/save $dir/rapport.pdf 
