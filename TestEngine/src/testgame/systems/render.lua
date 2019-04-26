function getRequiredComponents()
  return {"position", "size"}
end

function render(g)
  for name, entity in pairs(entities) do
    pos = entity.position
    size = entity.size
    g:fillRect(pos.x, pos.y, size.width, size.height)
  end
end