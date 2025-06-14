pipeline {
    agent any

    tools {
        jdk 'Java 17'
        maven 'Maven 3.8'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/tedkouuu/World-Of-Tanks.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
    }
}
