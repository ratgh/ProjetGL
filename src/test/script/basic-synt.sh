#! /bin/sh

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# exemple de définition d'une fonction
test_synt_invalide () {
    # $1 = premier argument.
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "$1 : PASS."
    else
        echo "$1 : ERROR."
        test_synt "$1";
        exit 1
    fi
}    


test_synt_valid() {

if test_synt "$1" 2>&1 | grep -q -e ':[0-9][0-9]*:'
then
        echo "$1 : ERROR."
        test_synt "$1" 2>&1;
        exit 1
else
    if test_synt "$1" 2>&1 | grep -q -e 'Exception in thread'
    then
        echo "$1 : ERROR."
        test_synt "$1" 2>&1;
        exit 1
    else
        echo "$1 : PASS."
    fi
fi
}

for cas_de_test in src/test/deca/syntax/invalid/provided/*.deca
do
    test_synt_invalide "$cas_de_test"
    echo ""
done

for cas_de_test in src/test/deca/syntax/valid/provided/*.deca
do
    test_synt_valid "$cas_de_test"
    echo ""
done

echo "OK !"
