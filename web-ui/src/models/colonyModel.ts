import { useState, useCallback } from 'react'

export default function colonyModel() {
  const [colonyData, setColonyData] = useState<API.ColonyData>()
  const setClusterId = useCallback((value: number)=>{
    setColonyData({...colonyData, clusterId: value})
  },[])

  return {
    colonyData,
    setClusterId,
    setColonyData
  }
}