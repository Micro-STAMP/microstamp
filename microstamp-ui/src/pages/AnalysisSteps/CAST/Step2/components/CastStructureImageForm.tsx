import { BiImageAdd, BiTrash, BiUpload } from 'react-icons/bi';
import { toast } from 'sonner';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';


import { deleteControlStructureImage, getControlStructureImages, saveControlStructureImage } from '@/http/Step2/ControlStructureImage';
import CastSection from '@components/CastSection';

interface Props { analysisId: string; }

export default function CastStructureImageForm({ analysisId }: Props) {
    const queryClient = useQueryClient();

    const { data: images, isLoading } = useQuery({
        queryKey: ['analysis-images', analysisId],
        queryFn: () => getControlStructureImages(analysisId)
    });

    const currentImage = images && images.length > 0 ? images[0] : null;

    const { mutateAsync: uploadImage, isPending: isUploading } = useMutation({
        mutationFn: async (file: File) => {
            return await saveControlStructureImage(file, analysisId);
        },
        onSuccess: () => {
            toast.success("Structure Image uploaded successfully!");
            queryClient.invalidateQueries({ queryKey: ['analysis-images', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error uploading image.")
    });

    const { mutateAsync: removeImage, isPending: isDeleting } = useMutation({
        mutationFn: async (id: string) => {
            if (window.confirm("Are you sure you want to remove the control structure image?")) {
                return await deleteControlStructureImage(id);
            }
            return Promise.reject(new Error("Cancelled"));
        },
        onSuccess: () => {
            toast.success("Image removed.");
            queryClient.invalidateQueries({ queryKey: ['analysis-images', analysisId] });
        },
        onError: (err: any) => {
            if (err.message !== "Cancelled") toast.error(err.message);
        }
    });

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        if (!file.type.startsWith('image/')) {
            toast.error("Please upload a valid image file (PNG, JPG).");
            return;
        }

        await uploadImage(file);
        
        e.target.value = ''; 
    };

    return (
        <CastSection title="Control Structure Image" tooltipInfo="Upload the final visual diagram of your Control Structure." hideAddButton={true}>
            {isLoading ? (
                <div style={{ color: '#9ca3af', fontSize: '14px', padding: '10px' }}>Loading image...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                    
                    {!currentImage ? (
                        <div style={{ 
                            border: '2px dashed #4b5563', 
                            borderRadius: '8px', 
                            padding: '40px 20px', 
                            textAlign: 'center', 
                            backgroundColor: '#1f2937',
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            gap: '12px'
                        }}>
                            <BiImageAdd size={48} color="#9ca3af" />
                            <div>
                                <p style={{ margin: 0, color: '#d1d5db', fontWeight: 'bold' }}>No image has been added yet</p>
                                <p style={{ margin: '4px 0 0 0', color: '#9ca3af', fontSize: '12px' }}>Upload a PNG or JPG of your Control Structure</p>
                            </div>
                            
                            <label style={{ 
                                marginTop: '12px',
                                backgroundColor: 'var(--color-yellow)', 
                                color: '#000', 
                                padding: '8px 16px', 
                                borderRadius: '4px', 
                                fontWeight: 'bold',
                                cursor: isUploading ? 'not-allowed' : 'pointer',
                                display: 'flex',
                                alignItems: 'center',
                                gap: '8px',
                                opacity: isUploading ? 0.7 : 1
                            }}>
                                <BiUpload size={18} />
                                {isUploading ? 'Uploading...' : 'Choose Image'}
                                <input 
                                    type="file" 
                                    accept="image/png, image/jpeg" 
                                    style={{ display: 'none' }} 
                                    onChange={handleFileChange}
                                    disabled={isUploading}
                                />
                            </label>
                        </div>
                    ) : (
                        <div style={{ 
                            border: '1px solid #4b5563', 
                            borderRadius: '8px', 
                            overflow: 'hidden',
                            backgroundColor: '#111827',
                            display: 'flex',
                            flexDirection: 'column'
                        }}>
                            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '12px 16px', backgroundColor: '#1f2937', borderBottom: '1px solid #374151' }}>
                                <span style={{ color: '#d1d5db', fontSize: '14px', fontWeight: 'bold' }}>Current Diagram</span>
                                <button 
                                    onClick={() => removeImage(currentImage.id)} 
                                    disabled={isDeleting}
                                    style={{ background: 'transparent', border: 'none', color: '#ef4444', cursor: isDeleting ? 'not-allowed' : 'pointer', display: 'flex', alignItems: 'center', gap: '4px', fontSize: '14px' }}
                                >
                                    <BiTrash size={16} /> {isDeleting ? 'Removing...' : 'Remove'}
                                </button>
                            </div>
                            
                            <div style={{ padding: '20px', display: 'flex', justifyContent: 'center' }}>
                                <img 
                                    src={currentImage.base64.startsWith('data:image') ? currentImage.base64 : `data:image/png;base64,${currentImage.base64}`} 
                                    alt="Control Structure" 
                                    style={{ maxWidth: '100%', maxHeight: '600px', objectFit: 'contain', borderRadius: '4px' }} 
                                />
                            </div>
                        </div>
                    )}

                </div>
            )}
        </CastSection>
    );
}