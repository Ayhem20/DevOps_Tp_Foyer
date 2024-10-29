pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    def repoUrl = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
                    def branch = 'chaima-sassi'
                    withCredentials([usernamePassword(credentialsId: '8f9b2f59-5031-4710-ba76-f57fadc1a5de', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                        checkout([$class: 'GitSCM',
                            branches: [[branch: "${branch}"]],
                            userRemoteConfigs: [[url: "${repoUrl}", credentialsId: '8f9b2f59-5031-4710-ba76-f57fadc1a5de']]
                        ])
                    }
                }
            }
        }
    }
}
