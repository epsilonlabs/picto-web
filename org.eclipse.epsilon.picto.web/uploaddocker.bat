docker image build . -t picto-web
docker tag picto-web:latest alfayohannisyorkacuk/picto-web:latest
docker push alfayohannisyorkacuk/picto-web:latest
