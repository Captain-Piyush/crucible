import { useState } from 'react';
import axios from 'axios';

export default function ProjectAnalyzer() {
    const [description, setDescription] = useState('');
    const [analysis, setAnalysis] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleAnalyze = async () => {
        setLoading(true);
        try {
            const response = await axios.post('http://localhost:8080/api/projects/breakdown', {
                user_description: description
            });
            setAnalysis(response.data);
        } catch (error) {
            console.error("Error fetching breakdown:", error);
        }
        setLoading(false);
    };

    return (
        <div className="max-w-3xl mx-auto p-6 bg-white shadow-md rounded-lg mt-10">
            <h2 className="text-2xl font-bold mb-4 text-gray-800">Crucible AI Architect</h2>
            <textarea
                className="w-full p-3 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 mb-4"
                rows="4"
                placeholder="Describe your project (e.g., 'I need a ride-sharing app with real-time tracking...')"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
            />
            <button
                onClick={handleAnalyze}
                disabled={loading}
                className="w-full bg-blue-600 text-white font-semibold py-2 rounded-md hover:bg-blue-700 disabled:bg-blue-300"
            >
                {loading ? 'Analyzing...' : 'Generate Modules'}
            </button>

            {analysis && (
                <div className="mt-6 border-t pt-4">
                    {analysis.is_clear ? (
                        <div>
                            <h3 className="text-xl font-semibold text-green-600 mb-3">Project Modules Generated:</h3>
                            <div className="space-y-3">
                                {analysis.modules.map((mod, idx) => (
                                    <div key={idx} className="p-4 bg-gray-50 border rounded-md">
                                        <h4 className="font-bold text-gray-800">{mod.module_name} <span className="text-sm font-normal text-gray-500">(Priority: {mod.priority})</span></h4>
                                        <p className="text-gray-600 mt-1">{mod.description}</p>
                                    </div>
                                ))}
                            </div>
                        </div>
                    ) : (
                        <div>
                            <h3 className="text-xl font-semibold text-amber-600 mb-3">Clarification Needed:</h3>
                            <ul className="list-disc pl-5 space-y-2">
                                {analysis.clarifying_questions.map((cq, idx) => (
                                    <li key={idx} className="text-gray-700">
                                        <strong>{cq.question}</strong>
                                        <p className="text-sm text-gray-500 mt-1">Options: {cq.options.join(', ')}</p>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}