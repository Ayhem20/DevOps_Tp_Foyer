pipeline {
    agent any

    stages {
        // Stage to check out the code from GitHub
        stage('Checkout Code') {
            steps {
                script {
                    def repoUrl = 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git'
                    def branch = 'ibtihel'
                    withCredentials([usernamePassword(credentialsId: '608aa325-9043-49fb-afb2-6e953ec29495', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                        checkout([$class: 'GitSCM',
                            branches: [[name: "${branch}"]],
                            userRemoteConfigs: [[url: "${repoUrl}", credentialsId: '608aa325-9043-49fb-afb2-6e953ec29495']]
                        ])
                    }
                }
            }
        }
    }
    }