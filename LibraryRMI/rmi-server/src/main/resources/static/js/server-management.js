const { createApp } = Vue

createApp({
    data() {
        return {
            serverStatus: 'offline',
            serverLogs: '',
            pollingInterval: null
        }
    },
    methods: {
        async getServerStatus() {
            try {
                const response = await axios.get('/api/server/status')
                this.serverStatus = response.data.status
                this.serverLogs = response.data.logs
                this.$nextTick(() => {
                    const logsContainer = this.$refs.logsContainer
                    logsContainer.scrollTop = logsContainer.scrollHeight
                })
            } catch (error) {
                console.error('Error fetching server status:', error)
            }
        },
        async startServer() {
            try {
                await axios.post('/api/server/start')
                await this.getServerStatus()
            } catch (error) {
                console.error('Error starting server:', error)
            }
        },
        async stopServer() {
            try {
                await axios.post('/api/server/stop')
                await this.getServerStatus()
            } catch (error) {
                console.error('Error stopping server:', error)
            }
        },
        async restartServer() {
            try {
                await axios.post('/api/server/restart')
                await this.getServerStatus()
            } catch (error) {
                console.error('Error restarting server:', error)
            }
        },
        startPolling() {
            this.pollingInterval = setInterval(this.getServerStatus, 2000)
        },
        stopPolling() {
            if (this.pollingInterval) {
                clearInterval(this.pollingInterval)
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