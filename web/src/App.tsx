import { useState } from 'react'
import './App.css'

const API_BASE = 'http://localhost'

const sampleRequest = {
  longitude: 126.9,
  latitude: 37.5,
  pollutants: [
    { type: 'PM25', value: 35.5 },
    { type: 'PM10', value: 45.0 }
  ],
  deviceId: 'web-client'
}

function App() {
  const [responseA, setResponseA] = useState<string>('')
  const [responseB, setResponseB] = useState<string>('')
  const [loading, setLoading] = useState<{ a: boolean; b: boolean }>({ a: false, b: false })

  const callServerA = async () => {
    setLoading(prev => ({ ...prev, a: true }))
    try {
      const res = await fetch(`${API_BASE}:8081/api/a/air-quality`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(sampleRequest)
      })
      const data = await res.json()
      setResponseA(JSON.stringify(data, null, 2))
    } catch (err) {
      setResponseA(`Error: ${err}`)
    } finally {
      setLoading(prev => ({ ...prev, a: false }))
    }
  }

  const callServerB = async () => {
    setLoading(prev => ({ ...prev, b: true }))
    try {
      const res = await fetch(`${API_BASE}:8082/api/b/air-quality`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(sampleRequest)
      })
      const data = await res.json()
      setResponseB(JSON.stringify(data, null, 2))
    } catch (err) {
      setResponseB(`Error: ${err}`)
    } finally {
      setLoading(prev => ({ ...prev, b: false }))
    }
  }

  return (
    <div className="container">
      <h1>Air Quality API Tester</h1>

      <div className="buttons">
        <div className="api-section">
          <button onClick={callServerA} disabled={loading.a}>
            {loading.a ? 'Loading...' : 'Call Server A (MongoDB)'}
          </button>
          <pre>{responseA || 'No response yet'}</pre>
        </div>

        <div className="api-section">
          <button onClick={callServerB} disabled={loading.b}>
            {loading.b ? 'Loading...' : 'Call Server B (Kafka)'}
          </button>
          <pre>{responseB || 'No response yet'}</pre>
        </div>
      </div>
    </div>
  )
}

export default App
