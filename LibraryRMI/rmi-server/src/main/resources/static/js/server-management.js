const { createApp } = Vue

createApp({
    data() {
        return {
            serverStatus: 'offline',
            serverLogs: '',
            pollingInterval: null,
            loading: false,
            lastError: null
        }
    },
    methods: {
        async getServerStatus() {
            try {
                const response = await axios.get('/api/server/status')
                this.serverStatus = response.data.status
                this.serverLogs = response.data.logs
                this.lastError = null
                this.$nextTick(() => {
                    const logsContainer = this.$refs.logsContainer
                    logsContainer.scrollTop = logsContainer.scrollHeight
                })
            } catch (error) {
                console.error('Error fetching server status:', error)
                this.lastError = 'Errore durante il recupero dello stato del server'
                this.stopPolling()
            }
        },
        resetError() {
            this.lastError = null
        },
        async executeServerAction(action, actionName) {
            this.loading = true
            this.resetError()
            try {
                await axios.post(`/api/server/${action}`)
                await this.getServerStatus()
            } catch (error) {
                console.error(`Error ${actionName} server:`, error)
                this.lastError = `Errore durante ${actionName} del server`
            } finally {
                this.loading = false
            }
        },
        async startServer() {
            await this.executeServerAction('start', "l'avvio")
        },
        async stopServer() {
            await this.executeServerAction('stop', "l'arresto")
        },
        async restartServer() {
            await this.executeServerAction('restart', 'il riavvio')
        },
        startPolling() {
            if (!this.pollingInterval) {
                this.pollingInterval = setInterval(this.getServerStatus, 2000)
            }
        },
        stopPolling() {
            if (this.pollingInterval) {
                clearInterval(this.pollingInterval)
                this.pollingInterval = null
            }
        },
        resumePolling() {
            if (this.lastError) {
                this.resetError()
                this.startPolling()
                this.getServerStatus()
            }
        }
    },
    mounted() {
        this.getServerStatus()
        this.startPolling()
    },
    beforeUnmount() {
        this.stopPolling()
    }
}).mount('#app')
