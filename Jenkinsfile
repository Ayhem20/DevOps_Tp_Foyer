pipeline {
    agent any  // Utilise n'importe quel agent ou nœud disponible

    environment {
        // Définissez des variables d'environnement si nécessaire
        GIT_URL = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
    }

    stages {
        stage('Cloner le code depuis GitHub') {
            steps {
                // Configurez l'accès Git avec un token si nécessaire
                git url: "${env.GIT_URL}", credentialsId: 'github-token'
            }
        }

        stage('Construire') {
            steps {
                echo 'Construction du projet...'
                // Ajoutez ici la commande pour compiler ou construire votre projet si applicable
                // Exemple pour un projet Maven : sh 'mvn clean install'
            }
        }

        stage('Exécuter les tests') {
            steps {
                echo 'Exécution des tests...'
                // Exécutez les tests (par exemple, les tests unitaires)
                // Exemple pour Maven : sh 'mvn test'
            }
        }

        stage('Déployer') {
            steps {
                echo 'Déploiement de l’application...'
                // Ajoutez ici les étapes de déploiement
                // Exemple pour copier des fichiers ou déployer sur un serveur
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé.'
        }
        success {
            echo 'Pipeline exécuté avec succès !'
        }
        failure {
            echo 'Échec du pipeline.'
        }
    }
}
