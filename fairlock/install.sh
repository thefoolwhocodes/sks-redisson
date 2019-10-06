#!/bin/bash

install()
{
    sudo apt install maven
}

package()
{
    mvn package
    cp ./target/fairlock-0.0.1-jar-with-dependencies.jar .
}

run()
{
   java -jar ./fairlock-0.0.1-jar-with-dependencies.jar
}

deploy()
{
    install
    package
    run
}

usage()
{
    echo "    Usage"
    echo "    ./install <options>"
    echo "    Options can be following"
    echo "        install: Installs dependencies and creates basic setup"
    echo "        package: Package Jar"
    echo "        run: Runs the utility"
    echo "        deploy: Does installation, packaging and run"
}

handler()
{
    case $1 in
        install)
            echo -n "install- Installing dependencies and creating setup"
            install
            ;;
        package)
            echo -n "install- Packaging software"
            package
            ;;
        run)
            echo -n "install- Running with existing package"
            run
            ;;
        deploy)
            echo -n "install- Deploying complete setup"
            deploy
           ;;
        *)
           echo -n "install - I am sorry, received wrong choice."
           usage
           ;;
    esac
}

if [ "$#" -ne 1 ]; then
    usage
    exit 1
fi

handler $1
