'use client';

import { useEffect, useState } from 'react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Select, SelectTrigger, SelectValue, SelectContent, SelectItem } from '@/components/ui/select';
import dynamic from 'next/dynamic';
import { motion } from 'framer-motion';

const Chart = dynamic(() => import('react-chartjs-2').then(mod => mod.Line), {
  ssr: false,
  loading: () => <div>Loading chart...</div>
});

export default function DashboardPage() {
  const [searchParams, setSearchParams] = useState({
    serviceType: '',
    status: '',
    date: '',
    userName: ''
  });

  const [requests, setRequests] = useState([]);
  const [favorites, setFavorites] = useState([]);
  const [statistics, setStatistics] = useState({
    totalRequests: 0,
    pendingRequests: 0,
    completedRequests: 0
  });

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const response = await fetch('/api/dashboard');
      const data = await response.json();
      setRequests(data.requests);
      setFavorites(data.favorites);
      setStatistics(data.statistics);
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    }
  };

  const chartData = {
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
    datasets: [
      {
        label: 'Requests',
        data: [12, 19, 3, 5, 2, 3],
        fill: false,
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1
      }
    ]
  };

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-4xl font-bold mb-8">Dashboard</h1>
      
      {/* Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <motion.div
          whileHover={{ scale: 1.05 }}
          className="transition-all duration-200"
        >
          <Card className="p-6 bg-gradient-to-br from-blue-500 to-blue-600 text-white">
            <h3 className="text-lg font-semibold">Total Requests</h3>
            <p className="text-3xl font-bold">{statistics.totalRequests}</p>
          </Card>
        </motion.div>
        
        <motion.div
          whileHover={{ scale: 1.05 }}
          className="transition-all duration-200"
        >
          <Card className="p-6 bg-gradient-to-br from-yellow-500 to-yellow-600 text-white">
            <h3 className="text-lg font-semibold">Pending</h3>
            <p className="text-3xl font-bold">{statistics.pendingRequests}</p>
          </Card>
        </motion.div>
        
        <motion.div
          whileHover={{ scale: 1.05 }}
          className="transition-all duration-200"
        >
          <Card className="p-6 bg-gradient-to-br from-green-500 to-green-600 text-white">
            <h3 className="text-lg font-semibold">Completed</h3>
            <p className="text-3xl font-bold">{statistics.completedRequests}</p>
          </Card>
        </motion.div>
      </div>

      {/* Search Section */}
      <Card className="p-6 mb-8">
        <h2 className="text-2xl font-semibold mb-4">Advanced Search</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <Input
            placeholder="Service Type"
            value={searchParams.serviceType}
            onChange={(e) => setSearchParams({...searchParams, serviceType: e.target.value})}
          />
          <Select
            value={searchParams.status}
            onValueChange={(value) => setSearchParams({...searchParams, status: value})}
          >
            <SelectTrigger>
              <SelectValue placeholder="Select Status" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">All Status</SelectItem>
              <SelectItem value="pending">Pending</SelectItem>
              <SelectItem value="completed">Completed</SelectItem>
            </SelectContent>
          </Select>
          <Input
            type="date"
            value={searchParams.date}
            onChange={(e) => setSearchParams({...searchParams, date: e.target.value})}
          />
          <Input
            placeholder="User Name"
            value={searchParams.userName}
            onChange={(e) => setSearchParams({...searchParams, userName: e.target.value})}
          />
        </div>
        <Button className="mt-4">Search</Button>
      </Card>

      {/* Chart */}
      <Card className="p-6 mb-8">
        <h2 className="text-2xl font-semibold mb-4">Request Trends</h2>
        <div className="h-[300px]">
          <Chart data={chartData} options={{ maintainAspectRatio: false }} />
        </div>
      </Card>

      {/* Favorites Section */}
      <Card className="p-6">
        <h2 className="text-2xl font-semibold mb-4">Favorite Services</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {favorites.map((favorite: any) => (
            <Card key={favorite.id} className="p-4">
              <h3 className="font-semibold">{favorite.service.name}</h3>
              <p className="text-sm text-gray-600">{favorite.service.description}</p>
              <Button variant="outline" size="sm" className="mt-2">
                Remove from Favorites
              </Button>
            </Card>
          ))}
        </div>
      </Card>
    </div>
  );
}