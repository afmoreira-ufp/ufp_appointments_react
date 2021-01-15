# UFP Appointments React

Projeto que suporta a marcação de atendimentos.\
Utiliza a API da UFP, consumindo o WSDL e convertendo para classes java (ver plugin maven-jaxb2-plugin no pom.xml).\
Utiliza o projeto Spring Boot, com as bibliotecas starter, data-jpa, webmvc e security.\
O Frontend foi feito com React.js e o resultado da transpilação para javascript nativo é utilizado como recurso do projeto Spring Boot (ver plugin frontend-maven-plugin no pom.xml).\
Suporta base de dados em memória (H2) ou persistente (Postgres). A escolha pela base de dados é feita por meio do perfil spring ativo. Pode ser mudado no ficheiro appplication.properties ou como variável de ambiente da execução do binário (p.ex. ```java -jar -Dspring.profiles.active=dev```).\
Suporta também o acesso aos serviços do Google Calendar (necessário criar um projeto na Consola de Desenvolvedores do Google e criar credenciais).\
Utiliza docker-compose para integrar a aplicação principal a um container já configurado com postgres e a base de dados adequada.

##Como utilizar em modo de desenvolvimento
Deve garantir que o node.js está instalado e globalmente acessível.
Garantir que o perfil spring no application.properties possui o valor dev, para ler as variáveis ambientais do ficheiro application-dev.properties. Executar o projeto spring normalmente.\
Aceder a pasta ufp_react no terminal e executar o comando ```npm start```.

Foi criado um utilizador com username teacher e password 12345 para poderem ter acesso ao backoffice dos professores.  

##Como criar binário para produção
Utilizar os lifecycles clean e package do maven. 
